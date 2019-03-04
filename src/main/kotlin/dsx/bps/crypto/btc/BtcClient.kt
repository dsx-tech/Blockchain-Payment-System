package dsx.bps.crypto.btc

import java.util.*
import java.math.BigDecimal
import dsx.bps.core.Payment
import dsx.bps.core.Currency
import dsx.bps.crypto.common.CoinClient
import dsx.bps.crypto.btc.datamodel.BtcTxOutput

class BtcClient: CoinClient {

    constructor(): super()
    constructor(conf: Properties): super(conf)
    constructor(confPath: String): super(confPath)

    override val currency = Currency.BTC

    override val rpc: BtcRpc
    override val blockchainListener: BtcBlockchainListener

    init {
        val user = config.getProperty("BTC.user", "user")
        val pass = config.getProperty("BTC.password", "password")
        val host = config.getProperty("BTC.ip", "127.0.0.1")
        val port = config.getProperty("BTC.port", "18443")
        val url = "http://$user:$pass@$host:$port/"
        rpc = BtcRpc(url)

        blockchainListener = BtcBlockchainListener(rpc)
    }

    override fun getBalance(): BigDecimal = rpc.getBalance()

    override fun getAddress(): String = rpc.getNewAddress()

    override fun sendPayment(payment: Payment) {
        if (payment.currency != currency)
            throw IllegalArgumentException("CoinClient is $currency, but provided payment is ${payment.currency}")

        val output = BtcTxOutput(payment.address, payment.amount)

        // TODO: implement a reliable payment sending
        var rawTx = rpc.createRawTransaction(output)
        val fundedRawTx = rpc.fundRawTransaction(rawTx)
        rawTx = fundedRawTx.hex
        rawTx = rpc.signRawTransactionWithWallet(rawTx)
        payment.fee = fundedRawTx.fee
        payment.rawTx = rawTx
        payment.txId = rpc.sendRawTransaction(rawTx)
    }
}