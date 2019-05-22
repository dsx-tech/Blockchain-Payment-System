package dsx.bps.core

import dsx.bps.core.datamodel.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.Coins
import dsx.bps.crypto.Explorers
import io.reactivex.Observer
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import java.util.*

class BlockchainPaymentSystemManager(confPath: String = DEFAULT_CONFIG_PATH) {

    companion object {
        private val DEFAULT_CONFIG = Properties() // TODO: init default configuration
        private val DEFAULT_CONFIG_PATH = System.getProperty("user.home") + File.separator + "bps" + File.separator + "bps.properties"
    }

    private val config = Properties(DEFAULT_CONFIG)
    private val coins: Coins
    private val explorers: Explorers
    private val invoiceProcessor: InvoiceProcessor
    private val paymentProcessor: PaymentProcessor

    init {
        try {
            val f = File(confPath)
            if (f.exists()) {
                FileInputStream(f).use { config.load(it) }
            }
        } catch (ex: Exception) {
            throw RuntimeException(ex.message) // TODO: replace with logging
        }

        coins = Coins(config)
        explorers = Explorers(coins, config)
        invoiceProcessor = InvoiceProcessor(this)
        paymentProcessor = PaymentProcessor(this)

        explorers.subscribe(invoiceProcessor)
    }

    fun getBalance(currency: Currency): BigDecimal {
        return coins.balance(currency)
    }

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): String {
        val payment = paymentProcessor.createPayment(currency, amount, address, tag)
        val tx = coins.send(currency, amount, address, tag)
        paymentProcessor.updatePayment(payment.id, tx)
        return payment.id
    }

    fun createInvoice(currency: Currency, amount: BigDecimal): String {
        val tag = coins.tag(currency)
        val address = coins.address(currency)
        val invoice = invoiceProcessor.createInvoice(currency, amount, address, tag)
        return invoice.id
    }

    fun getPayment(id: String): Payment? = paymentProcessor.getPayment(id)

    fun getInvoice(id: String): Invoice? = invoiceProcessor.getInvoice(id)

    fun getTx(currency: Currency, txid: TxId): Tx {
        return coins.tx(currency, txid)
    }

    fun getTxs(currency: Currency, txids: List<TxId>): List<Tx> {
        return coins.txs(currency, txids)
    }

    fun subscribe(observer: Observer<Tx>) {
        explorers.subscribe(observer)
    }

    fun subscribeOnCoin(currency: Currency, observer: Observer<Tx>) {
        explorers.subscribeOnCoin(currency, observer)
    }
}