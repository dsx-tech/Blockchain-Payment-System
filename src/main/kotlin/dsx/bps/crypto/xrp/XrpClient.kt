package dsx.bps.crypto.xrp

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.common.CoinClient
import dsx.bps.crypto.xrp.datamodel.XrpTxPayment
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

        blockchainListener  = XrpBlockchainListener(rpc, account)
    }

    override fun getBalance(): BigDecimal = rpc.getBalance(account)

    override fun getAddress(): String = account

    override fun getTx(txid: TxId): Tx =
        rpc.getTransaction(txid.hash)

    override fun sendPayment(payment: Payment) {
        val tx = payment
            .let { createTransaction(it.amount, it.address, it.tag) }
            .let { rpc.sign(privateKey, it) }
            .let { rpc.submit(it) }

        with(payment) {
            txid = TxId(tx.hash(), tx.sequence)
            hex = tx.hex
            fee = tx.fee()
        }
    }

    private fun createTransaction(amount: BigDecimal, address: String, tag: Int?): XrpTxPayment {
        val fee = rpc.getTxCost()
        val seq = rpc.getSequence(account)
        return XrpTxPayment(account, amount, address, fee.toPlainString(), seq, tag)
    }
}