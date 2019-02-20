package dsx.bps.core

import java.util.*
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal

class BlockchainPaymentSystemManager(confPath: String) {

    companion object {
        private val DEFAULT_CONFIG = Properties() // TODO: init default configuration
        private val DEFAULT_CONFIG_PATH = System.getProperty("user.home") + File.separator + "bps" + File.separator + "bps.properties"
    }

    constructor(): this(DEFAULT_CONFIG_PATH)

    private val config = Properties(DEFAULT_CONFIG)
    private val coins: MutableMap<Currency, CoinClient> = mutableMapOf()

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
    }

    private fun getClient(currency: Currency): CoinClient = coins[currency]
        ?: throw Exception("Currency ${currency.name} isn't specified in configuration file or isn't supported.")

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String): String {
        val coin = getClient(currency)

        // TODO: implement Payment-Processor, create Payment and send it into client
        val payment = coin.sendPayment(amount, address)
        return payment.id
    }

    fun createInvoice(currency: Currency, amount: BigDecimal): String {
        val coin = getClient(currency)

        // TODO: implement Payment-Processor, create Payment and send it into client
        val invoice = coin.createInvoice(amount)
        return invoice.id
    }

    fun getBalance(currency: Currency): BigDecimal {
        val coin = getClient(currency)
        return coin.getBalance()
    }

}