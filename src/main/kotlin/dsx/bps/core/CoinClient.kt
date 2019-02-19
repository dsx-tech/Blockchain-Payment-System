package dsx.bps.core

import dsx.bps.crypto.BlockchainListener
import dsx.bps.crypto.InvoiceListener
import java.math.BigDecimal

abstract class CoinClient {

    abstract val currency: Currency

    // TODO: implement storage for invoices and payments
    protected val invoices: HashMap<String, Invoice> = HashMap()
    protected val payments: HashMap<String, Payment> = HashMap()

    protected abstract val blockchainListener: BlockchainListener
    protected abstract val invoiceListener: InvoiceListener

    fun getInvoice(id: String): Invoice? = invoices[id]

    fun getInvoices(): Map<String, Invoice> = invoices

    fun getPayment(id: String): Payment? = payments[id]

    fun getPayments(): Map<String, Payment> = payments

    open fun sendInvoice(amount: BigDecimal): Invoice {
        val address = getNewAddress()
        val inv = Invoice(currency, amount, address)

        println("please, send $amount ${currency.name} to $address")

        invoices.putIfAbsent(inv.id, inv)
        invoiceListener.addInvoice(inv)

        return inv
    }

    abstract fun sendPayment(address: String, amount: BigDecimal): Payment

    abstract fun getNewAddress(): String

    abstract fun getBalance(): BigDecimal
}