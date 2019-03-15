package dsx.bps.crypto.xrp

import dsx.bps.core.datamodel.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.CoinClient
import dsx.bps.crypto.xrp.datamodel.*
import java.math.BigDecimal
import java.util.*

class XrpClient: CoinClient {

    constructor(): super()
    constructor(conf: Properties): super(conf)
    constructor(confPath: String): super(confPath)

    override val currency = Currency.XRP

    private val account: String
    private val privateKey: String
    private val passPhrase: String

    override val rpc: XrpRpc
    override val blockchainListener: XrpBlockchainListener

    init {
        account = config.getProperty("XRP.account", "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh")
        privateKey = config.getProperty("XRP.privateKey", "snoPBrXtMeMyMHUVTgbuqAfg1SUTb")
        passPhrase = config.getProperty("XRP.passPhrase", "masterpassphrase")

        val host = config.getProperty("XRP.ip", "127.0.0.1")
        val port = config.getProperty("XRP.port", "51234")
        val url = "http://$host:$port/"
        rpc = XrpRpc(url)

        val frequency = config.getProperty("XRP.frequency", "5000").toLong()
        blockchainListener = XrpBlockchainListener(this, frequency)
    }

    override fun getBalance(): BigDecimal = rpc.getBalance(account)

    override fun getAddress(): String = account

    override fun getTx(txid: TxId): Tx {
        val xrtTx = rpc.getTransaction(txid.hash)
        return constructTx(xrtTx)
    }

    override fun sendPayment(payment: Payment) {
        val tx = payment
            .let { createTransaction(it.amount, it.address, it.tag) }
            .let { rpc.sign(privateKey, it) }
            .let { rpc.submit(it) }

        with(payment) {
            txid = TxId(tx.hash, tx.sequence)
            fee = BigDecimal(tx.fee)
            hex = tx.hex
        }
    }

    private fun createTransaction(amount: BigDecimal, address: String, tag: Int?): XrpTxPayment {
        val fee = rpc.getTxCost()
        val seq = rpc.getSequence(account)
        return XrpTxPayment(account, amount, address, fee.toPlainString(), seq, tag)
    }

    fun getLastLedger(): XrpLedger = rpc.getLastLedger()

    fun getLedger(hash: String): XrpLedger = rpc.getLedger(hash)

    fun getAccountTxs(indexMin: Long, indexMax: Long): XrpAccountTxs = rpc.getAccountTxs(account, indexMin, indexMax)

    fun constructTx(xrpAccountTx: XrpAccountTx): Tx {
        val tx = xrpAccountTx.tx
        val meta = xrpAccountTx.meta

        return object: Tx {

            override fun currency() = Currency.XRP

            override fun hash() = tx.hash

            override fun index() = tx.sequence

            override fun amount() = meta.deliveredAmount

            override fun tag() = tx.destinationTag

            override fun destination() = tx.destination

            override fun fee() = BigDecimal(tx.fee)

            override fun status() = if (xrpAccountTx.validated) TxStatus.CONFIRMED else TxStatus.VALIDATING
        }
    }

    fun constructTx(xrtTx: XrpTx): Tx = object: Tx {

        override fun currency() = Currency.XRP

        override fun hash() = xrtTx.hash

        override fun index() = xrtTx.sequence

        override fun amount() = xrtTx.meta?.deliveredAmount ?: xrtTx.amount

        override fun destination() = xrtTx.destination

        override fun tag() = xrtTx.destinationTag

        override fun fee() = BigDecimal(xrtTx.fee)

        override fun status() = if (xrtTx.validated) TxStatus.CONFIRMED else TxStatus.VALIDATING
    }
}