package dsx.bps.crypto.xrp

import dsx.bps.core.datamodel.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.xrp.datamodel.*
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

class XrpCoin(conf: Properties): Coin(conf) {

    override val currency = Currency.XRP
    override val rpc: XrpRpc
    override val address: String
    private val privateKey: String

    init {
        address = config.getProperty("XRP.address", "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh")
        privateKey = config.getProperty("XRP.privateKey", "snoPBrXtMeMyMHUVTgbuqAfg1SUTb")

        val host = config.getProperty("XRP.ip", "127.0.0.1")
        val port = config.getProperty("XRP.port", "51234")
        val url = "http://$host:$port/"
        rpc = XrpRpc(url)
    }

    override val balance: BigDecimal
        get() = rpc.getBalance(address)

    override fun tag(): Int? = Random.nextInt(0, Int.MAX_VALUE)

    override fun tx(txid: TxId): Tx {
        val xrtTx = rpc.getTransaction(txid.hash)
        return constructTx(xrtTx)
    }

    override fun send(amount: BigDecimal, destination: String, tag: Int?): Tx {
        return createTransaction(amount, destination, tag)
            .let { rpc.sign(privateKey, it) }
            .let { rpc.submit(it) }
            .let { constructTx(it) }
    }

    private fun createTransaction(amount: BigDecimal, destination: String, tag: Int?): XrpTxPayment {
        val fee = rpc.getTxCost()
        val seq = rpc.getSequence(address)
        return XrpTxPayment(address, amount, destination, fee.toPlainString(), seq, tag)
    }

    fun getLastLedger(): XrpLedger = rpc.getLastLedger()

    fun getLedger(hash: String): XrpLedger = rpc.getLedger(hash)

    fun getAccountTxs(indexMin: Long, indexMax: Long): XrpAccountTxs = rpc.getAccountTxs(address, indexMin, indexMax)

    fun constructTx(xrpAccountTx: XrpAccountTx): Tx {
        val tx = xrpAccountTx.tx
        val delivered = xrpAccountTx.meta.deliveredAmount
        var amount = BigDecimal.ZERO
        if (delivered?.currency == currency.name)
            amount = delivered.value

        return object: Tx {

            override fun currency() = Currency.XRP

            override fun hash() = tx.hash

            override fun index() = tx.sequence

            override fun amount() = amount

            override fun tag() = tx.destinationTag

            override fun destination() = tx.destination

            override fun fee() = BigDecimal(tx.fee)

            override fun status() = if (xrpAccountTx.validated) TxStatus.CONFIRMED else TxStatus.VALIDATING
        }
    }

    fun constructTx(xrpTx: XrpTx): Tx {
        val delivered = xrpTx.meta?.deliveredAmount
        var amount = BigDecimal.ZERO
        if (delivered?.currency == currency.name)
            amount = delivered.value

        return object: Tx {

            override fun currency() = Currency.XRP

            override fun hash() = xrpTx.hash

            override fun index() = xrpTx.sequence

            override fun amount() = amount

            override fun destination() = xrpTx.destination

            override fun tag() = xrpTx.destinationTag

            override fun fee() = BigDecimal(xrpTx.fee)

            override fun status() = if (xrpTx.validated) TxStatus.CONFIRMED else TxStatus.VALIDATING
        }
    }
}