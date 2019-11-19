package dsx.bps.crypto.trx

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.TrxConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.*
import dsx.bps.crypto.common.CoinClient
import dsx.bps.crypto.trx.datamodel.TrxBlock
import dsx.bps.crypto.trx.datamodel.TrxTx
import java.io.File
import java.math.BigDecimal
import kotlin.random.Random

class TrxClient: CoinClient {

    override val currency = Currency.TRX
    override val config: Config

    private val account: String
    private val accountAddress: String
    private val privateKey: String

    override val rpc: TrxRpc
    override val blockchainListener: TrxBlockchainListener

    private val confirmations: Int

    constructor() {
        config = Config()

        account = "TW4hF7TVhme1STRC2NToDA41RxCx1H2HbS" // Base58Checksum representation of account
        accountAddress = "41dc6c2bc46639e5371fe99e002858754604cf5847"
        privateKey = "92f451c194301b4a1eae3a818cc181e1a319656dcdf6760cdbe35c54b05bb3ec"

        val url = "http://127.0.0.1:18190/wallet/"
        rpc = TrxRpc(url)

        blockchainListener = TrxBlockchainListener(this, 3000)
        confirmations = 3
    }

    constructor(conf: Config) {
        config = conf

        account = config[TrxConfig.account]
        accountAddress = config[TrxConfig.accountAddress]
        privateKey = config[TrxConfig.privateKey]

        val host = config[TrxConfig.host]
        val port = config[TrxConfig.port]
        val url = "http://$host:$port/wallet/"
        rpc = TrxRpc(url)

        val frequency = config[TrxConfig.frequency]
        blockchainListener = TrxBlockchainListener(this, frequency)
        confirmations = config[TrxConfig.confirmations]
    }

    constructor(configPath: String) {
        val initConfig = Config()
        val configFile = File(configPath)
        config = with (initConfig) {
            addSpec(TrxConfig)
            from.yaml.file(configFile)
        }

        config.validateRequired()

        account = config[TrxConfig.account]
        accountAddress = config[TrxConfig.accountAddress]
        privateKey = config[TrxConfig.privateKey]

        val host = config[TrxConfig.host]
        val port = config[TrxConfig.port]
        val url = "http://$host:$port/wallet/"
        rpc = TrxRpc(url)

        val frequency = config[TrxConfig.frequency]
        blockchainListener = TrxBlockchainListener(this, frequency)
        confirmations = config[TrxConfig.confirmations]
    }

    constructor(trxRpc: TrxRpc, trxBlockchainListener: TrxBlockchainListener, configPath: String) {
        val initConfig = Config()
        val configFile = File(configPath)
        config = with (initConfig) {
            addSpec(TrxConfig)
            from.yaml.file(configFile)
        }

        config.validateRequired()

        account = config[TrxConfig.account]
        accountAddress = config[TrxConfig.accountAddress]
        privateKey = config[TrxConfig.privateKey]

        rpc = trxRpc
        blockchainListener = trxBlockchainListener

        confirmations = config[TrxConfig.confirmations]
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