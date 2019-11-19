package dsx.bps.core

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.InvoiceProcessorConfig
import dsx.bps.config.PaymentProcessorConfig
import dsx.bps.core.datamodel.*
import dsx.bps.crypto.Coins
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.math.BigDecimal
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BlockchainPaymentSystemManager {

    companion object {
        private val DEFAULT_CONFIG_PATH = this::class.java.getResource("/BpsConfig.yaml").path
    }

    private val coins: Coins
    private val emitter: Observable<Tx>
    private val invoiceProcessor: InvoiceProcessor
    private val paymentProcessor: PaymentProcessor

    constructor(confPath: String = DEFAULT_CONFIG_PATH) {
        val configFile = File(confPath)

        coins = Coins(configFile)

        val emitters = coins.coins.values.map { it.getTxEmitter() }
        val threadPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        emitter = Observable
            .merge(emitters)
            .observeOn(Schedulers.from(threadPool))

        val invoiceProcessorConfig = with(Config()) {
            addSpec(InvoiceProcessorConfig)
            from.yaml.file(configFile)
        }
        invoiceProcessorConfig.validateRequired()

        val paymentProcessorConfig = with(Config()) {
            addSpec(PaymentProcessorConfig)
            from.yaml.file(configFile)
        }
        paymentProcessorConfig.validateRequired()

        invoiceProcessor = InvoiceProcessor(this, invoiceProcessorConfig)
        paymentProcessor = PaymentProcessor(this, paymentProcessorConfig)
    }

    constructor(coinsMap: Coins, invoiceProcessor: InvoiceProcessor,
                paymentProcessor: PaymentProcessor) {
        coins = coinsMap

        val emitters = coins.coins.values.map { it.getTxEmitter() }
        val threadPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        emitter = Observable
            .merge(emitters)
            .observeOn(Schedulers.from(threadPool))

        this.invoiceProcessor = invoiceProcessor
        this.paymentProcessor = paymentProcessor
    }

    fun getBalance(currency: Currency): BigDecimal {
        return coins.getBalance(currency)
    }

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): String {
        val payment = paymentProcessor.createPayment(currency, amount, address, tag)
        val tx = coins.sendPayment(currency, amount, address, tag)
        paymentProcessor.updatePayment(payment.id, tx)
        return payment.id
    }

    fun createInvoice(currency: Currency, amount: BigDecimal): String {
        val tag = coins.getTag(currency)
        val address = coins.getAddress(currency)
        val invoice = invoiceProcessor.createInvoice(currency, amount, address, tag)
        return invoice.id
    }

    fun getPayment(id: String): Payment? = paymentProcessor.getPayment(id)

    fun getInvoice(id: String): Invoice? = invoiceProcessor.getInvoice(id)

    fun getTx(currency: Currency, txid: TxId): Tx {
        return coins.getTx(currency, txid)
    }

    fun getTxs(currency: Currency, txids: List<TxId>): List<Tx> {
        return coins.getTxs(currency, txids)
    }

    fun subscribe(observer: Observer<Tx>) {
        emitter.subscribe(observer)
    }
}