package dsx.bps.core

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.InvoiceProcessorConfig
import dsx.bps.config.PaymentProcessorConfig
import dsx.bps.config.currencies.EnabledCoinsConfig
import dsx.bps.core.datamodel.*
import dsx.bps.crypto.common.CoinClient
import dsx.bps.crypto.common.CoinClientFactory
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

    private val coins: Map<Currency, CoinClient>
    private val emitter: Observable<Tx>
    private val invoiceProcessor: InvoiceProcessor
    private val paymentProcessor: PaymentProcessor

    constructor(confPath: String = DEFAULT_CONFIG_PATH){
        val configFile = File(confPath)

        val enabledCoinsConfig = with(Config()) {
            addSpec(EnabledCoinsConfig)
            from.yaml.file(configFile)
        }
        enabledCoinsConfig.validateRequired()

        val mutableCoinsMap: MutableMap<Currency, CoinClient> = mutableMapOf()

        for (coin in enabledCoinsConfig[EnabledCoinsConfig.coins]) {
            val coinConfig = with(Config()) {
                addSpec(coin.coinConfigSpec)
                from.yaml.file(configFile)
            }
            coinConfig.validateRequired()
            mutableCoinsMap[coin] = CoinClientFactory.createCoinClient(coin, coinConfig)
        }

        coins = mutableCoinsMap.toMap()

        val emitters = coins.values.map { it.getTxEmitter() }
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

    constructor(coinClients: Map<Currency, CoinClient>, invoiceProcessor: InvoiceProcessor,
                paymentProcessor: PaymentProcessor){
        coins = coinClients

        val emitters = coins.values.map { it.getTxEmitter() }
        val threadPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        emitter = Observable
            .merge(emitters)
            .observeOn(Schedulers.from(threadPool))

        this.invoiceProcessor = invoiceProcessor
        this.paymentProcessor = paymentProcessor
    }

    private fun getCoin(currency: Currency): CoinClient = coins[currency]
        ?: throw Exception("Currency ${currency.name} isn't specified in configuration file or isn't supported.")

    fun getBalance(currency: Currency): BigDecimal {
        val coin = getCoin(currency)
        return coin.getBalance()
    }

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): String {
        val coin = getCoin(currency)
        val payment = paymentProcessor.createPayment(currency, amount, address, tag)
        val tx = coin.sendPayment(amount, address, tag)
        paymentProcessor.updatePayment(payment.id, tx)
        return payment.id
    }

    fun createInvoice(currency: Currency, amount: BigDecimal): String {
        val coin = getCoin(currency)
        val tag = coin.getTag()
        val address = coin.getAddress()
        val invoice = invoiceProcessor.createInvoice(currency, amount, address, tag)
        return invoice.id
    }

    fun getPayment(id: String): Payment? = paymentProcessor.getPayment(id)

    fun getInvoice(id: String): Invoice? = invoiceProcessor.getInvoice(id)

    fun getTx(currency: Currency, txid: TxId): Tx {
        val coin = getCoin(currency)
        return coin.getTx(txid)
    }

    fun getTxs(currency: Currency, txids: List<TxId>): List<Tx> {
        val coin = getCoin(currency)
        return coin.getTxs(txids)
    }

    fun subscribe(observer: Observer<Tx>) {
        emitter.subscribe(observer)
    }
}