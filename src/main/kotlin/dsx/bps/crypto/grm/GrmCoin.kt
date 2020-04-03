package dsx.bps.crypto.grm

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.GrmService
import dsx.bps.DBservices.TxService
import dsx.bps.config.currencies.GrmConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.grm.datamodel.GrmFullAccountState
import dsx.bps.crypto.grm.datamodel.GrmInternalTxId
import dsx.bps.crypto.grm.datamodel.GrmRawTransaction
import dsx.bps.crypto.grm.datamodel.byteArrayToHex
import java.io.File
import java.math.BigDecimal
import kotlin.random.Random

class GrmCoin : Coin {

    override val currency: Currency = Currency.GRM
    override val config: Config

    private val accountAddress: String
    private val privateKey: String
    private val localPassword: String
    private val grmService: GrmService
    private val txService: TxService

    override val connector: GrmConnector
    override val explorer: GrmExplorer

    constructor(conf: Config, datasource: Datasource, txServ: TxService) {
        config = conf
        grmService = GrmService(datasource)
        txService = txServ

        accountAddress = config[GrmConfig.Coin.accountAddress]
        privateKey = config[GrmConfig.Coin.privateKey]
        localPassword = config[GrmConfig.Coin.localPassword]

        val tonClientConfig = File(config[GrmConfig.Connection.pathToTonClientConfig]).readText()
        val keyStorePath = config[GrmConfig.Connection.keyStorePath]
        val logVerbosityLevel = config[GrmConfig.Connection.logVerbosityLevel]
        connector = GrmConnector(tonClientConfig, keyStorePath, logVerbosityLevel)

        val frequency = config[GrmConfig.Explorer.frequency]
        explorer = GrmExplorer(this, datasource, txServ, frequency)
    }

    constructor(
        grmConnection: GrmConnector, grmExplorer: GrmExplorer,
        configPath: String, datasource: Datasource, txServ: TxService
    ) {
        grmService = GrmService(datasource)
        txService = txServ
        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(GrmConfig)
            from.yaml.file(configFile)
        }

        config.validateRequired()

        accountAddress = config[GrmConfig.Coin.accountAddress]
        privateKey = config[GrmConfig.Coin.privateKey]
        localPassword = config[GrmConfig.Coin.localPassword]

        connector = grmConnection
        explorer = grmExplorer
    }


    override fun getBalance(): BigDecimal = connector.getBalance(accountAddress)

    override fun getAddress(): String = accountAddress

    override fun getTag(): String? = Random.nextInt(0, Int.MAX_VALUE).toString()

    override fun getTx(txid: TxId): Tx {
        val grmRawTransaction = connector.getTransaction(accountAddress, txid)
        return constructDepositTx(grmRawTransaction)
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
        val queryInfo = connector.sendPaymentQuery(
            accountAddress, privateKey,
            localPassword, amount, address, tag, 100
        )
        val queryEstimateFees = connector.getQueryEstimateFees(queryInfo.id, true)

        //TODO: Implement actualize payment tx and status
        return object : Tx {
            override fun currency(): Currency = Currency.GRM
            override fun hash(): String = byteArrayToHex(queryInfo.bodyHash)
            override fun txid(): TxId = TxId(hash(), -1)
            override fun amount(): BigDecimal = amount
            override fun destination(): String = address
            override fun fee(): BigDecimal = BigDecimal(queryEstimateFees.sourceFees.gasFee)

            override fun status(): TxStatus = TxStatus.VALIDATING
        }
    }

    fun getFullAccountState(): GrmFullAccountState {
        return connector.getFullAccountState(accountAddress)
    }

    fun getLastInternalTxId(): GrmInternalTxId {
        return connector.getLastInternalTxId(accountAddress)
    }

    /*
    Include startTx and not include untilTx
     */
    fun getAccountTxs(startTxId: GrmInternalTxId, untilTxId: GrmInternalTxId): Array<GrmRawTransaction> {
        val grmTxs = ArrayList<GrmRawTransaction>()

        var tmpTxId = startTxId
        var allNewTxsProcessed = false

        while (!allNewTxsProcessed) {
            val olderAccountTxs = connector.getOlderAccountTxs(
                accountAddress, tmpTxId
            )
            for (grmOlderTx in olderAccountTxs.transactions) {
                if (grmOlderTx.transactionId == untilTxId) {
                    allNewTxsProcessed = true
                    break
                }
                grmTxs.add(grmOlderTx)
            }
            if (olderAccountTxs.previousTransactionId.lt == 0L &&
                olderAccountTxs.previousTransactionId.hash ==
                "0000000000000000000000000000000000000000000000000000000000000000"
            ) {
                allNewTxsProcessed = true
            }
            tmpTxId = olderAccountTxs.previousTransactionId
        }
        return grmTxs.toTypedArray()
    }

    fun constructDepositTx(grmTx: GrmRawTransaction): Tx {

        return object : Tx {
            override fun currency() = Currency.GRM

            override fun hash() = grmTx.transactionId.hash

            override fun txid(): TxId = TxId(hash(), grmTx.transactionId.lt)

            override fun amount(): BigDecimal {
                var amount = grmTx.inMsg.value
                for (msg in grmTx.outMsg) {
                    amount -= msg.value + msg.ihrFee + msg.fwdFee
                }
                return BigDecimal(amount)
            }

            override fun destination() = grmTx.inMsg.destination

            override fun paymentReference(): String? = grmTx.inMsg.msgText

            override fun fee() = BigDecimal(grmTx.fee)

            //TODO: Верно только для депозитов. Для вывода средств хз нужно думать как отслеживать вывод и скорее всего править.
            override fun status() = TxStatus.CONFIRMED

        }
    }
}