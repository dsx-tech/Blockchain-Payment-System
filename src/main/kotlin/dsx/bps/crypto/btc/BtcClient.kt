package dsx.bps.crypto.btc

import dsx.bps.core.datamodel.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.btc.datamodel.*
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

    private val confirmations: Int

    init {
        val user = config.getProperty("BTC.user", "user")
        val pass = config.getProperty("BTC.password", "password")
        val host = config.getProperty("BTC.ip", "127.0.0.1")
        val port = config.getProperty("BTC.port", "18443")
        val url = "http://$user:$pass@$host:$port/"
        rpc = BtcRpc(url)

        val frequency = config.getProperty("BTC.frequency", "5000").toLong()
        blockchainListener = BtcBlockchainListener(this, frequency)

        confirmations = config.getProperty("BTC.confirmations", "1").toInt()
    }

    override fun getBalance(): BigDecimal = rpc.getBalance()

    override fun getAddress(): String = rpc.getNewAddress()

    override fun getTag(): Int? = null

    override fun getTx(txid: TxId): Tx {
        val btcTx = rpc.getTransaction(txid.hash)
        return constructTx(btcTx, txid)
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

    fun getBestBlockHash(): String = rpc.getBestBlockHash()

    fun getBlock(hash: String): BtcBlock = rpc.getBlock(hash)

    fun listSinceBlock(hash: String): BtcListSinceBlock = rpc.listSinceBlock(hash)

    fun constructTx(btcTx: BtcTx, txid: TxId): Tx {
        val detail = btcTx
            .details
            .first { detail -> detail.vout == txid.index }

        return object: Tx {
            override fun currency() = Currency.BTC

            override fun hash() = btcTx.hash

            override fun index() = detail.vout

            override fun amount() = detail.amount.abs()

            override fun destination() = detail.address

            override fun fee() = detail.fee.abs()

            override fun status() = when {
                btcTx.confirmations < 0 -> TxStatus.REJECTED
                btcTx.confirmations < confirmations -> TxStatus.VALIDATING
                else -> TxStatus.CONFIRMED
            }
        }
    }

    fun constructTx(btcTxSinceBlock: BtcTxSinceBlock): Tx = object: Tx {

        override fun currency() = Currency.BTC

        override fun hash() = btcTxSinceBlock.hash

        override fun index() = btcTxSinceBlock.vout

        override fun amount() = btcTxSinceBlock.amount

        override fun destination() = btcTxSinceBlock.address

        override fun fee() = btcTxSinceBlock.fee

        override fun status() = when {
            btcTxSinceBlock.confirmations < 0 -> TxStatus.REJECTED
            btcTxSinceBlock.confirmations < confirmations -> TxStatus.VALIDATING
            else -> TxStatus.CONFIRMED
        }
    }
}