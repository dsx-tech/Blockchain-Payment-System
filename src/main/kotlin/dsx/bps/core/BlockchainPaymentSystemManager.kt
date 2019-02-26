package dsx.bps.core

import java.util.*
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import dsx.bps.crypto.common.CoinClient
import dsx.bps.crypto.common.CoinClientFactory

class BlockchainPaymentSystemManager(confPath: String = DEFAULT_CONFIG_PATH) {

    companion object {
        private val DEFAULT_CONFIG = Properties() // TODO: init default configuration
        private val DEFAULT_CONFIG_PATH = System.getProperty("user.home") + File.separator + "bps" + File.separator + "bps.properties"
    }

    private val config = Properties(DEFAULT_CONFIG)
    private val coins: MutableMap<Currency, CoinClient> = mutableMapOf()
    private val invoiceProcessor = InvoiceProcessor()
    private val paymentProcessor = PaymentProcessor()

    init {
        try {
            val f = File(confPath)
            if (f.exists()) {
                FileInputStream(f).use { config.load(it) }
            }
        } catch (ex: Exception) {
            throw RuntimeException(ex.message) // TODO: replace with logging
        }

        Currency
            .values()
            .filter { config.getProperty(it.name) == "1" }
            .forEach {
                try {
                    coins[it] = CoinClientFactory.createCoinClient(it, config)
                } catch (ex: Exception) {
                    throw RuntimeException(ex.message) // TODO: replace with logging
                }
            }

        val emitters = coins.values.map { it.getTxEmitter() }
        Observable
            .merge(emitters)
            .observeOn(Schedulers.computation())
            .subscribe(invoiceProcessor)
    }

    private fun getClient(currency: Currency): CoinClient = coins[currency]
        ?: throw Exception("Currency ${currency.name} isn't specified in configuration file or isn't supported.")

    fun getBalance(currency: Currency): BigDecimal {
        val coin = getClient(currency)
        return coin.getBalance()
    }

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String): String {
        val coin = getClient(currency)
        val payment = paymentProcessor.createPayment(currency, amount, address)
        coin.sendPayment(payment)
        return payment.id
    }

    fun createInvoice(currency: Currency, amount: BigDecimal): String {
        val coin = getClient(currency)
        val address = coin.getAddress()
        val invoice = invoiceProcessor.createInvoice(currency, amount, address)
        return invoice.id
    }

    fun getPayment(id: String): Payment? = paymentProcessor.getPayment(id)

    fun getInvoice(id: String): Invoice? = invoiceProcessor.getInvoice(id)
}