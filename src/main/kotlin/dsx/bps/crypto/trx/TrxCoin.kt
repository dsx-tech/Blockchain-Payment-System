package dsx.bps.crypto.trx

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.TrxConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.*
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.common.Explorer
import dsx.bps.crypto.trx.datamodel.TrxBlock
import dsx.bps.crypto.trx.datamodel.TrxTx
import java.io.File
import java.math.BigDecimal
import kotlin.random.Random

class TrxCoin: Coin {

    override val currency = Currency.TRX
    override val config: Config

    private val account: String
    private val accountAddress: String
    private val privateKey: String

    override val rpc: TrxRpc
    override val explorer: Explorer

    private val confirmations: Int

    constructor(conf: Config) {
        if (conf.specs.contains(TrxConfig))
            conf.validateRequired()
        else
            throw Exception("Config not contain TrxConfig spec")

        config = conf

        account = config[TrxConfig.Coin.account]
        accountAddress = config[TrxConfig.Coin.accountAddress]
        privateKey = config[TrxConfig.Coin.privateKey]

        val host = config[TrxConfig.Connection.host]
        val port = config[TrxConfig.Connection.port]
        val url = "http://$host:$port/wallet/"
        rpc = TrxRpc(url)

        confirmations = config[TrxConfig.Coin.confirmations]

        val frequency = config[TrxConfig.Explorer.frequency]
        explorer = TrxExplorer(this, frequency)
    }

    constructor(configPath: String) {
        val initConfig = Config()
        val configFile = File(configPath)
        config = with (initConfig) {
            addSpec(TrxConfig)
            from.yaml.file(configFile)
        }
        config.validateRequired()

        account = config[TrxConfig.Coin.account]
        accountAddress = config[TrxConfig.Coin.accountAddress]
        privateKey = config[TrxConfig.Coin.privateKey]

        val host = config[TrxConfig.Connection.host]
        val port = config[TrxConfig.Connection.port]
        val url = "http://$host:$port/wallet/"
        rpc = TrxRpc(url)

        confirmations = config[TrxConfig.Coin.confirmations]

        val frequency = config[TrxConfig.Explorer.frequency]
        explorer = TrxExplorer(this, frequency)
    }

    constructor(trxRpc: TrxRpc, trxExplorer: TrxExplorer, configPath: String) {
        val initConfig = Config()
        val configFile = File(configPath)
        config = with (initConfig) {
            addSpec(TrxConfig)
            from.yaml.file(configFile)
        }
        config.validateRequired()

        account = config[TrxConfig.Coin.account]
        accountAddress = config[TrxConfig.Coin.accountAddress]
        privateKey = config[TrxConfig.Coin.privateKey]

        confirmations = config[TrxConfig.Coin.confirmations]

        rpc = trxRpc
        explorer = trxExplorer
    }

    override fun getBalance(): BigDecimal = rpc.getBalance(accountAddress)

    override fun getAddress(): String = accountAddress

    override fun getTag(): Int? = Random.nextInt(0, Int.MAX_VALUE)

    override fun getTx(txid: TxId): Tx {
        val trxTx = rpc.getTransactionById(txid.hash)
        return constructTx(trxTx)
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: Int?): Tx {
        val tx = rpc.createTransaction(address, accountAddress, amount)
            .let {
                it.rawData.data = tag?.toString(16)
                rpc.getTransactionSign(privateKey, it)
            }

        val result = rpc.broadcastTransaction(tx)

        if (result.success) {
            return constructTx(tx)
        } else {
            throw RuntimeException("Unable to broadcast TRX transaction $tx with response $result")
        }
    }

    fun getNowBlock(): TrxBlock = rpc.getNowBlock()

    fun getBlockByNum(num: Int): TrxBlock = rpc.getBlockByNum(num)

    fun getBlockById(hash: String): TrxBlock = rpc.getBlockById(hash)

    fun constructTx(trxTx: TrxTx): Tx {
        val contract = trxTx.rawData.contract.first()
        val value = contract.parameter.value

        val txInfo = rpc.getTransactionInfoById(trxTx.hash)
        val lastBlock = rpc.getNowBlock()

        return object: Tx {
            override fun currency() = Currency.TRX

            override fun hash() = trxTx.hash

            override fun amount() = value.amount

            override fun destination() = value.toAddress

            override fun fee() = BigDecimal.ZERO

            override fun status(): TxStatus {
                val conf = lastBlock.blockHeader.rawData.number - txInfo.blockNumber
                return when {
                    conf < confirmations -> TxStatus.VALIDATING
                    else -> TxStatus.CONFIRMED
                }
            }

            override fun tag() = trxTx.rawData.data?.toInt(16)
        }
    }
}