package dsx.bps.crypto.btc

import java.math.BigDecimal
import dsx.bps.core.Payment
import dsx.bps.core.Currency
import dsx.bps.core.CoinClient

class BtcClient(url: String? = null): CoinClient() {

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

    override fun sendPayment(address: String, amount: BigDecimal): Payment {
        val payment = Payment(currency, amount, address)

        val out = BtcJSON.BtcTxOutput(address, amount)

        var rawTx = rpc.createRawTransaction(listOf(), listOf(out))
        val fundedRawTx = rpc.fundRawTransaction(rawTx)
        payment.fee = fundedRawTx.fee
        rawTx = rpc.signRawTransactionWithWallet(rawTx)
        payment.rawTx = rawTx
        payment.txId = rpc.sendRawTransaction(rawTx)

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