package dsx.bps.core

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.config.BPSConfig
import dsx.bps.config.DatabaseConfig
import dsx.bps.config.InvoiceProcessorConfig
import dsx.bps.config.PaymentProcessorConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Invoice
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.CoinsManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.math.BigDecimal
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.xml.crypto.Data

class BlockchainPaymentSystemManager {

    companion object {
        private val DEFAULT_CONFIG_PATH = this::class.java.getResource("/BpsConfig.yaml").path
    }

    private val coinsManager: CoinsManager
    private val emitter: Observable<Tx>
    private val datasource = Datasource()
    private val invoiceProcessor: InvoiceProcessor
    private val paymentProcessor: PaymentProcessor

    constructor(confPath: String = DEFAULT_CONFIG_PATH) {
        val configFile = File(confPath)

        val databaseConfig = with(Config()) {
            addSpec(DatabaseConfig)
            from.yaml.file(configFile)
        }
        databaseConfig.validateRequired()

        datasource.initConnection(databaseConfig)
        coinsManager = CoinsManager(configFile, datasource)

        val bpsConfig = with(Config()) {
            addSpec(BPSConfig)
            from.yaml.file(configFile)
        }
        bpsConfig.validateRequired()
        val threadPool: ExecutorService = Executors.newFixedThreadPool(bpsConfig[BPSConfig.threadsForInvoiceObserver])
        emitter = coinsManager.getAllEmitters().observeOn(Schedulers.from(threadPool))

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

        invoiceProcessor = InvoiceProcessor(this, invoiceProcessorConfig, datasource)
        paymentProcessor = PaymentProcessor(this, paymentProcessorConfig, datasource)
    }

    constructor(coinsManager: CoinsManager, invoiceProcessor: InvoiceProcessor, paymentProcessor: PaymentProcessor) {
        this.coinsManager = coinsManager

        val threadPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        emitter = this.coinsManager.getAllEmitters().observeOn(Schedulers.from(threadPool))

        this.invoiceProcessor = invoiceProcessor
        this.paymentProcessor = paymentProcessor
    }

    fun getBalance(currency: Currency): BigDecimal {
        return coinsManager.getBalance(currency)
    }

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): String {
        val payment = paymentProcessor.createPayment(currency, amount, address, tag)
        val tx = coinsManager.sendPayment(currency, amount, address, tag)
        paymentProcessor.updatePayment(payment.id, tx)
        return payment.id
    }

    fun createInvoice(currency: Currency, amount: BigDecimal): String {
        val tag = coinsManager.getTag(currency)
        val address = coinsManager.getAddress(currency)
        val invoice = invoiceProcessor.createInvoice(currency, amount, address, tag)
        return invoice.id
    }

    fun getPayment(id: String): Payment? = paymentProcessor.getPayment(id)

    fun getInvoice(id: String): Invoice? = invoiceProcessor.getInvoice(id)

    fun getTx(currency: Currency, txid: TxId): Tx {
        return coinsManager.getTx(currency, txid)
    }

    fun getTxs(currency: Currency, txids: List<TxId>): List<Tx> {
        return coinsManager.getTxs(currency, txids)
    }

    fun subscribe(observer: Observer<Tx>) {
        emitter.subscribe(observer)
    }
}