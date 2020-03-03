package dsx.bps.crypto.xrp

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.XrpConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.xrp.datamodel.*
import java.io.File
import java.math.BigDecimal
import kotlin.random.Random

class XrpCoin: Coin {

    override val currency = Currency.XRP
    override val config: Config

    private val account: String
    private val privateKey: String
    private val passPhrase: String

    override val connection: XrpRpc
    override val explorer: XrpExplorer

    constructor(conf: Config) {
        config = conf

        account = config[XrpConfig.Coin.account]
        privateKey = config[XrpConfig.Coin.privateKey]
        passPhrase = config[XrpConfig.Coin.passPhrase]

        val host = config[XrpConfig.Connection.host]
        val port = config[XrpConfig.Connection.port]
        val url = "http://$host:$port/"
        connection = XrpRpc(url)

        val frequency = config[XrpConfig.Explorer.frequency]
        explorer = XrpExplorer(this, frequency)
    }

    constructor(xrpRpc: XrpRpc, xrpExplorer: XrpExplorer, configPath: String) {
        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(XrpConfig)
            from.yaml.file(configFile)
        }
        config.validateRequired()

        account = config[XrpConfig.Coin.account]
        privateKey = config[XrpConfig.Coin.privateKey]
        passPhrase = config[XrpConfig.Coin.passPhrase]

        connection = xrpRpc
        explorer = xrpExplorer
    }

    override fun getBalance(): BigDecimal = connection.getBalance(account)

    override fun getAddress(): String = account

    override fun getTag(): Int? = Random.nextInt(0, Int.MAX_VALUE)

    override fun getTx(txid: TxId): Tx {
        val xrtTx = connection.getTransaction(txid.hash)
        return constructTx(xrtTx)
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: Int?): Tx {
        return createTransaction(amount, address, tag)
            .let { connection.sign(privateKey, it) }
            .let { connection.submit(it) }
            .let { constructTx(it) }
    }

    private fun createTransaction(amount: BigDecimal, address: String, tag: Int?): XrpTxPayment {
        val fee = connection.getTxCost()
        val seq = connection.getSequence(account)
        return XrpTxPayment(account, amount, address, fee.toPlainString(), seq, tag)
    }

    fun getLastLedger(): XrpLedger = connection.getLastLedger()

    fun getLedger(hash: String): XrpLedger = connection.getLedger(hash)

    fun getAccountTxs(indexMin: Long, indexMax: Long): XrpAccountTxs =
        connection.getAccountTxs(account, indexMin, indexMax)

    fun constructTx(xrpAccountTx: XrpAccountTx): Tx {
        val tx = xrpAccountTx.tx
        val delivered = xrpAccountTx.meta.deliveredAmount
        var amount = BigDecimal.ZERO
        if (delivered?.currency == currency.name)
            amount = delivered.value

        return object : Tx {

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