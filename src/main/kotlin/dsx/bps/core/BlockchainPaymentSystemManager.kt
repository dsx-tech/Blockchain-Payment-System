package dsx.bps.core

import dsx.bps.core.datamodel.*
import dsx.bps.crypto.common.CoinClient
import dsx.bps.crypto.common.CoinClientFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import java.util.Properties
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BlockchainPaymentSystemManager {

    companion object {
        private val DEFAULT_CONFIG = Properties() // TODO: init default configuration
        private val DEFAULT_CONFIG_PATH = System.getProperty("user.home") + File.separator + "bps" + File.separator + "bps.properties"
    }

    private val config: Properties = Properties(DEFAULT_CONFIG)
    private val coins: Map<Currency, CoinClient>
    private val emitter: Observable<Tx>
    private val invoiceProcessor: InvoiceProcessor
    private val paymentProcessor: PaymentProcessor

    constructor(confPath: String = DEFAULT_CONFIG_PATH){

        try {
            val f = File(confPath)
            if (f.exists()) {
                FileInputStream(f).use { config.load(it) }
            }
        } catch (ex: Exception) {
            throw RuntimeException(ex.message) // TODO: replace with logging
        }

        coins = Currency
            .values()
            .filter { config.getProperty(it.name) == "1" }
            .mapNotNull {
                try {
                    it to CoinClientFactory.createCoinClient(it, config)
                } catch (ex: Exception) {
                    println("Failed to create client for ${it.name}:\n" + ex.message)
                    null
                }
            }.toMap()

        val emitters = coins.values.map { it.getTxEmitter() }
        emitter = Observable
            .merge(emitters)
            .observeOn(Schedulers.computation())

        invoiceProcessor = InvoiceProcessor(this)
        paymentProcessor = PaymentProcessor(this)
    }

    constructor(coinClients: Map<Currency, CoinClient>, invoiceProcessor: InvoiceProcessor,
                paymentProcessor: PaymentProcessor, confPath: String = DEFAULT_CONFIG_PATH){
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