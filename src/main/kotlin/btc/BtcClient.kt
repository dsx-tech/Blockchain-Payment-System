package dsx.bps.kotlin.btc

import java.math.BigDecimal
import dsx.bps.kotlin.core.CoinClient
import dsx.bps.kotlin.core.Currency
import dsx.bps.kotlin.core.Invoice
import dsx.bps.kotlin.core.Payment
import java.util.*
import kotlin.Exception
import kotlin.collections.HashMap

class BtcClient(url: String? = null): CoinClient() {

    override val symbol = "BTC"
    override val currency = Currency.BTC

    internal val rpc: BtcRPC = if (url == null) {
        BtcRPC()
    } else {
        BtcRPC(url)
    }

    override val blockchainListener: BtcBlockchainListener = BtcBlockchainListener(rpc)
    override val invoiceListener: BtcInvoiceListener = BtcInvoiceListener(rpc)

    init {
        blockchainListener.addObserver(invoiceListener)
//        blockchainListener.addObserver( /* BalanceListener */ )
    }

    fun connect(url: String) {
        rpc.connect(url)
    }

    override fun sendPayment(outputs: Map<String, BigDecimal>): Payment {
        val outs = outputs.map { entry -> BtcJSON.BtcTxOutput(entry.key, entry.value) }

        var rawTx = rpc.createRawTransaction(listOf(), outs)
        rawTx = rpc.fundRawTransaction(rawTx)
        rawTx = rpc.signRawTransactionWithWallet(rawTx)
        val txid = rpc.sendRawTransaction(rawTx)

        val payment = Payment(currency, outputs, txid)
        payments.putIfAbsent(payment.id, payment)

        return payment
    }

    override fun getBalance(): BigDecimal {
        return rpc.balance
    }

    override fun getNewAddress(): String {
        return rpc.newAddress
    }

}