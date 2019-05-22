package dsx.bps.crypto.btc

import dsx.bps.core.datamodel.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.btc.datamodel.*
import dsx.bps.crypto.common.Coin
import java.math.BigDecimal
import java.util.*

class BtcCoin(conf: Properties): Coin(conf) {

    override val currency = Currency.BTC
    override val rpc: BtcRpc
    var confirmations: Int

    init {
        val user = config.getProperty("BTC.user", "user")
        val pass = config.getProperty("BTC.password", "password")
        val host = config.getProperty("BTC.ip", "127.0.0.1")
        val port = config.getProperty("BTC.port", "18443")
        val url = "http://$user:$pass@$host:$port/"
        rpc = BtcRpc(url)

        confirmations = config.getProperty("BTC.confirmations", "1").toInt()
    }

    override val address: String
        get() = rpc.getNewAddress()

    override val balance: BigDecimal
        get() = rpc.getBalance()

    override fun tx(txid: TxId): Tx {
        val btcTx = rpc.getTransaction(txid.hash)
        return constructTx(btcTx, txid)
    }

    override fun send(amount: BigDecimal, destination: String, tag: Int?): Tx {
        val tx = rpc.createRawTransaction(amount, destination)
            .let { rpc.fundRawTransaction(it) }
            // TODO: implement local tx sign
            .let { rpc.signRawTransactionWithWallet(it) }
            .let { rpc.sendRawTransaction(it) }
            .let { rpc.getTransaction(it) }

        val detail = tx
            .details
            .single { detail -> match(detail, amount, destination) }

        return constructTx(tx, TxId(tx.hash, detail.vout))
    }

    private fun match(detail: BtcTxDetail, amount: BigDecimal, address: String): Boolean =
            detail.address == address &&
            detail.category == "send" &&
            detail.amount.abs().compareTo(amount.abs()) == 0

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