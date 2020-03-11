package dsx.bps.crypto.grm

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.GrmConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.grm.datamodel.GrmFullAccountState
import dsx.bps.crypto.grm.datamodel.GrmInternalTxId
import dsx.bps.crypto.grm.datamodel.GrmRawTransaction
import java.io.File
import java.math.BigDecimal
import kotlin.random.Random

class GrmCoin : Coin {

    override val currency: Currency = Currency.GRM
    override val config: Config

    private val accountAddress: String

    override val connector: GrmConnector
    override val explorer: GrmExplorer

    constructor(conf: Config) {
        config = conf

        accountAddress = config[GrmConfig.Coin.accountAddress]

        val tonClientConfig = File(config[GrmConfig.Connection.pathToTonClientConfig]).readText()
        val keyStorePath = config[GrmConfig.Connection.keyStorePath]
        val logVerbosityLevel = config[GrmConfig.Connection.logVerbosityLevel]
        connector = GrmConnector(tonClientConfig, keyStorePath, logVerbosityLevel)

        val frequency = config[GrmConfig.Explorer.frequency]
        explorer = GrmExplorer(this, frequency)
    }

    constructor(grmConnection: GrmConnector, grmExplorer: GrmExplorer, configPath: String) {
        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(GrmConfig)
            from.yaml.file(configFile)
        }

        config.validateRequired()

        accountAddress = config[GrmConfig.Coin.accountAddress]

        connector = grmConnection
        explorer = grmExplorer
    }


    override fun getBalance(): BigDecimal = connector.getBalance(accountAddress)

    override fun getAddress(): String = accountAddress

    override fun getTag(): String? = Random.nextInt(0, Int.MAX_VALUE).toString()

    override fun getTx(txid: TxId): Tx {
        TODO("not implemented")
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
        TODO("not implemented")
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

    fun constructTx(grmTx: GrmRawTransaction): Tx {

        return object : Tx {
            override fun currency() = Currency.GRM

            override fun hash() = grmTx.transactionId.hash

            override fun txid(): TxId = TxId(hash(), grmTx.transactionId.lt)

            override fun amount() = BigDecimal(grmTx.inMsg.value)

            override fun destination() = grmTx.inMsg.destination

            override fun paymentReference(): String? = grmTx.inMsg.msgData.body

            override fun fee() = BigDecimal(grmTx.fee)

            //TODO: Верно только для депозитов. Для вывода средств хз нужно думать как отслеживать вывод и скорее всего править.
            override fun status() = TxStatus.CONFIRMED

        }
    }
}