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
import dsx.bps.crypto.xrp.datamodel.XrpAccountTx
import dsx.bps.crypto.xrp.datamodel.XrpAccountTxs
import dsx.bps.crypto.xrp.datamodel.XrpLedger
import dsx.bps.crypto.xrp.datamodel.XrpTx
import dsx.bps.crypto.xrp.datamodel.XrpTxPayment
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

    override val rpc: XrpRpc
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
        rpc = XrpRpc(url)

        val frequency = config[XrpConfig.Explorer.frequency]
        explorer = XrpExplorer(this, datasource, txServ, frequency)
    }

    constructor(xrpRpc: XrpRpc, xrpExplorer: XrpExplorer, configPath: String, datasource: Datasource, txServ: TxService) {
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

        rpc = xrpRpc
        explorer = xrpExplorer
    }

    override fun getBalance(): BigDecimal = rpc.getBalance(account)

    override fun getAddress(): String = account

    override fun getTag(): Int? = Random.nextInt(0, Int.MAX_VALUE)

    override fun getTx(txid: TxId): Tx {
        val xrtTx = rpc.getTransaction(txid.hash)
        return constructTx(xrtTx)
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: Int?): Tx {
        val xrpTx = createTransaction(amount, address, tag)
            .let { rpc.sign(privateKey, it) }
            .let { rpc.submit(it) }
        val tx = constructTx(xrpTx)
        val new = txService.add(tx.status(), tx.destination(), tx.tag(),
            tx.amount(), tx.fee(), tx.hash(), tx.index(), tx.currency())
        xrpService.add(tx.fee(), this.account, xrpTx.sequence, xrpTx.validated, new)
        return tx
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