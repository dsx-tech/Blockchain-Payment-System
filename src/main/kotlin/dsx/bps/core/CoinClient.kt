package dsx.bps.core

import java.util.*
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import dsx.bps.crypto.InvoiceListener
import dsx.bps.crypto.common.BlockchainListener

abstract class CoinClient {

    abstract val currency: Currency
    protected val config: Properties

    constructor() {
        config = Properties()
    }

    constructor(conf: Properties) {
        config = Properties(conf)
    }

    constructor(confPath: String) {
        val f = File(confPath)
        config = Properties()
        if (f.exists()) {
            FileInputStream(f).use { config.load(it) }
        }
    }

    // TODO: implement storage for invoices and payments
    protected val invoices: HashMap<String, Invoice> = HashMap()
    protected val payments: HashMap<String, Payment> = HashMap()

    protected abstract val blockchainListener: BlockchainListener
    protected abstract val invoiceListener: InvoiceListener

    open fun createInvoice(amount: BigDecimal): Invoice {
        val address = getNewAddress()
        val inv = Invoice(currency, amount, address)

        println("please, send $amount ${currency.name} to $address")

        invoices.putIfAbsent(inv.id, inv)
        invoiceListener.addInvoice(inv)

        return inv
    }

    abstract fun sendPayment(amount: BigDecimal, address: String): Payment

    abstract fun getNewAddress(): String

    abstract fun getBalance(): BigDecimal
}