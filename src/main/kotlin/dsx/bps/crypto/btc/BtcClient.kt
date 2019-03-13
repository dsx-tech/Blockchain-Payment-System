package dsx.bps.crypto.btc

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.btc.datamodel.BtcTxDetail
import dsx.bps.crypto.common.CoinClient
import java.math.BigDecimal
import java.util.*

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

    override fun getTx(txid: TxId): Tx {
        val tx = rpc.getTransaction(txid.hash)
        val detail = tx
            .details
            .single { detail -> detail.vout == txid.index }

        return object: Tx {
            override fun currency() = Currency.BTC

            override fun hash() = tx.hash

            override fun index() = detail.vout

            override fun amount() = detail.amount

            override fun destination() = detail.address

            override fun fee() = detail.fee?.abs() ?: BigDecimal.ZERO

            override fun confirmations() = tx.confirmations
        }
    }

    override fun sendPayment(payment: Payment) {
        val tx = payment
            .let { rpc.createRawTransaction(it.amount, it.address) }
            .let { rpc.fundRawTransaction(it) }
            .let { rpc.signRawTransactionWithWallet(it) }
            .let { rpc.sendRawTransaction(it) }
            .let { rpc.getTransaction(it) }

        val detail = tx
            .details
            .single { detail -> match(detail, payment) }

        with(payment) {
            txid = TxId(tx.hash, detail.vout)
            hex = tx.hex
            fee = tx.fee.abs()
        }
    }

    private fun match(detail: BtcTxDetail, payment: Payment): Boolean =
            detail.address == payment.address &&
            detail.category == "send" &&
            detail.amount == payment.amount.negate()
}