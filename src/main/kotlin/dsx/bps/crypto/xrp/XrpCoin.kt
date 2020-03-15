package dsx.bps.crypto.xrp

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.TxService
import dsx.bps.DBservices.XrpService
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
    private val xrpService: XrpService
    private val txService: TxService

    override val connector: XrpRpc
    override val explorer: XrpExplorer

    constructor(conf: Config, datasource: Datasource, txServ: TxService) {
        config = conf
        xrpService = XrpService(datasource)
        txService = txServ

        account = config[XrpConfig.Coin.account]
        privateKey = config[XrpConfig.Coin.privateKey]
        passPhrase = config[XrpConfig.Coin.passPhrase]

        val host = config[XrpConfig.Connection.host]
        val port = config[XrpConfig.Connection.port]
        val url = "http://$host:$port/"
        connector = XrpRpc(url)

        val frequency = config[XrpConfig.Explorer.frequency]
        explorer = XrpExplorer(this, datasource, txServ, frequency)
    }

    constructor(
        xrpRpc: XrpRpc, xrpExplorer: XrpExplorer, configPath: String,
        datasource: Datasource, txServ: TxService
    ) {
        xrpService = XrpService(datasource)
        txService = txServ
        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(XrpConfig)
            from.yaml.file(configFile)
        }
        config.validateRequired()

        account = config[XrpConfig.Coin.account]
        privateKey = config[XrpConfig.Coin.privateKey]
        passPhrase = config[XrpConfig.Coin.passPhrase]

        connector = xrpRpc
        explorer = xrpExplorer
    }

    override fun getBalance(): BigDecimal = connector.getBalance(account)

    override fun getAddress(): String = account

    override fun getTag(): String? = Random.nextInt(0, Int.MAX_VALUE).toString()

    override fun getTx(txid: TxId): Tx {
        val xrtTx = connector.getTransaction(txid.hash)
        return constructTx(xrtTx)
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
        val xrpTx = createTransaction(amount, address, tag)
            .let { connector.sign(privateKey, it) }
            .let { connector.submit(it) }
        val tx = constructTx(xrpTx)
        val new = txService.add(
            tx.status(), tx.destination(), tx.paymentReference(),
            tx.amount(), tx.fee(), tx.hash(), tx.index(), tx.currency()
        )
        xrpService.add(tx.fee(), this.account, xrpTx.sequence, xrpTx.validated, new)
        return tx
    }

    private fun createTransaction(amount: BigDecimal, address: String, tag: String?): XrpTxPayment {
        val fee = connector.getTxCost()
        val seq = connector.getSequence(account)
        return XrpTxPayment(account, amount, address, fee.toPlainString(), seq, tag?.toInt())
    }

    fun getLastLedger(): XrpLedger = connector.getLastLedger()

    fun getLedger(hash: String): XrpLedger = connector.getLedger(hash)

    fun getAccountTxs(indexMin: Long, indexMax: Long): XrpAccountTxs =
        connector.getAccountTxs(account, indexMin, indexMax)

    fun constructTx(xrpAccountTx: XrpAccountTx): Tx {
        val tx = xrpAccountTx.tx
        val delivered = xrpAccountTx.meta.deliveredAmount
        var amount = BigDecimal.ZERO
        if (delivered?.currency == currency.name)
            amount = delivered.value

        return object : Tx {

            override fun currency() = Currency.XRP

            override fun hash() = tx.hash

            override fun index() = tx.sequence.toLong()

            override fun amount() = amount

            override fun paymentReference(): String? = tx.destinationTag.toString()

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

            override fun index() = xrpTx.sequence.toLong()

            override fun amount() = amount

            override fun destination() = xrpTx.destination

            override fun paymentReference(): String? = xrpTx.destinationTag.toString()

            override fun fee() = BigDecimal(xrpTx.fee)

            override fun status() = if (xrpTx.validated) TxStatus.CONFIRMED else TxStatus.VALIDATING
        }
    }
}