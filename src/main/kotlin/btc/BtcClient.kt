package dsx.bps.kotlin.btc

import java.math.BigDecimal
import dsx.bps.kotlin.core.CoinClient
import dsx.bps.kotlin.core.Currency
import dsx.bps.kotlin.core.Invoice
import dsx.bps.kotlin.core.Payment
import java.util.*
import kotlin.Exception
import kotlin.collections.HashMap

class BtcClient(url: String? = null): CoinClient {

    override val symbol = "BTC"
    override val currency = Currency.BTC

    internal val rpc: BtcRPC = if (url == null) {
        BtcRPC()
    } else {
        BtcRPC(url)
    }
    private val blockchainListener: BtcBlockchainListener = BtcBlockchainListener(rpc)
    private val invoiceListener: BtcInvoiceListener = BtcInvoiceListener(rpc)

    // temp storage
    private val invoices: HashMap<UUID, Invoice> = HashMap() // TODO: implement storage for invoices and payments
    private val payments: HashMap<UUID, Payment> = HashMap()

    init {
        blockchainListener.addObserver(invoiceListener)
//        blockchainListener.addObserver( /* BalanceListener */ )
    }

    fun connect(url: String) {
        rpc.connect(url)
    }

    override fun sendInvoice(amount: BigDecimal): Invoice {
        val address = getNewAddress()
        val inv = Invoice(currency, amount, address)

        println("please, send $amount btc to $address")

        invoices.putIfAbsent(inv.id, inv)
        invoiceListener.addInvoice(inv)

        return inv
    }

    override fun getInvoice(id: String): Invoice? {
        try {
            return getInvoice(UUID.fromString(id))
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
        return null
    }

    override fun getInvoice(uuid: UUID): Invoice? {
        try {
            return invoices[uuid]
        } catch (e: Exception) {
            println(e.message)
        }
        return null
    }

    override fun sendPayment(address: String, amount: BigDecimal): String {
        return sendPayment(mapOf(address to amount))
    }

    override fun sendPayment(outputs: Map<String, BigDecimal>): String {
        val outs = outputs.map { entry -> BtcJSON.BtcTxOutput(entry.key, entry.value) }

        var rawTx = rpc.createRawTransaction(listOf(), outs)
        rawTx = rpc.fundRawTransaction(rawTx)
        rawTx = rpc.signRawTransactionWithWallet(rawTx)
        val txid = rpc.sendRawTransaction(rawTx)

        val payment = Payment(currency, outputs, txid)
        payments.putIfAbsent(payment.id, payment)

        return txid
    }

    override fun getBalance(): BigDecimal {
        return rpc.balance
    }

    override fun getNewAddress(): String {
        return rpc.newAddress
    }

}