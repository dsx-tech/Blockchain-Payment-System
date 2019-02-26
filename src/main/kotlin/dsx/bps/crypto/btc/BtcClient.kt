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

    internal val rpc: BtcRPC
    override val blockchainListener: BtcBlockchainListener

    init {
        val user = config.getProperty("BTC.user", "user")
        val pass = config.getProperty("BTC.password", "password")
        val host = config.getProperty("BTC.ip", "127.0.0.1")
        val port = config.getProperty("BTC.port", "18443")
        val url = "http://$user:$pass@$host:$port/"
        rpc = BtcRPC(url)

        blockchainListener = BtcBlockchainListener(rpc)
    }

    override fun sendPayment(amount: BigDecimal, address: String): Payment {
        val payment = Payment(currency, amount, address)

        val out = BtcTxOutput(address, amount)

        // TODO: implement a reliable payment sending
        var rawTx = rpc.createRawTransaction(listOf(), listOf(out))
        val fundedRawTx = rpc.fundRawTransaction(rawTx)
        payment.fee = fundedRawTx.fee
        rawTx = rpc.signRawTransactionWithWallet(fundedRawTx.hex)
        payment.rawTx = rawTx
        payment.txId = rpc.sendRawTransaction(rawTx)

        payments.putIfAbsent(payment.id, payment)

        return payment
    }

    override fun getBalance(): BigDecimal = rpc.balance

    override fun getAddress(): String = rpc.newAddress

}