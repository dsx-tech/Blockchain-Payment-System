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

    override val connection: GrmConnection
    override val explorer: GrmExplorer

    constructor(conf: Config) {
        config = conf

        accountAddress = config[GrmConfig.Coin.accountAddress]

        val tonClientConfig = File(config[GrmConfig.Connection.pathToTonClientConfig]).readText()
        val keyStorePath = config[GrmConfig.Connection.keyStorePath]
        val logVerbosityLevel = config[GrmConfig.Connection.logVerbosityLevel]
        connection = GrmConnection(tonClientConfig, keyStorePath, logVerbosityLevel)

        val frequency = config[GrmConfig.Explorer.frequency]
        explorer = GrmExplorer(this, frequency)
    }

    constructor(grmConnection: GrmConnection, grmExplorer: GrmExplorer, configPath: String) {
        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(GrmConfig)
            from.yaml.file(configFile)
        }

        config.validateRequired()

        accountAddress = config[GrmConfig.Coin.accountAddress]

        connection = grmConnection
        explorer = grmExplorer
    }


    override fun getBalance(): BigDecimal = connection.getBalance(accountAddress)

    override fun getAddress(): String = accountAddress

    override fun getTag(): Int? = Random.nextInt(0, Int.MAX_VALUE)

    override fun getTx(txid: TxId): Tx {
        TODO("not implemented")
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: Int?): Tx {
        TODO("not implemented")
    }

    fun getFullAccountState(): GrmFullAccountState {
        return connection.getFullAccountState(accountAddress)
    }

    fun getLastInternalTxId(): GrmInternalTxId {
        return connection.getLastInternalTxId(accountAddress)
    }

    fun getAccountTxs(sinceLastTxId: GrmInternalTxId): Array<GrmRawTransaction> {
        return connection.getAccountTxs(accountAddress, sinceLastTxId)
    }

    fun constructTx(grmTx: GrmRawTransaction): Tx {

        return object : Tx {
            override fun currency() = Currency.GRM

            override fun hash() = grmTx.transactionId.hash.toString()
            //TODO: Преобразование Long в Int небезопасено. Поменять на Long в интерфейсе Tx?
            override fun index() = grmTx.transactionId.lt.toInt()

            override fun amount() = BigDecimal(grmTx.inMsg.value)

            override fun destination() = grmTx.inMsg.destination

            override fun tag() = grmTx.inMsg.msgData.body.toString().toInt()

            override fun fee() = BigDecimal(grmTx.fee)

            override fun status() = TxStatus.CONFIRMED

        }
    }
}