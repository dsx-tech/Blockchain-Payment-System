package dsx.bps.crypto.eth

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.EthConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.eth.datamodel.EthBlock
import dsx.bps.crypto.eth.datamodel.EthTx
import java.io.File
import java.math.BigDecimal
import kotlin.random.Random


class EthCoin : Coin {
    override val currency = Currency.ETH
    override val config: Config

    private val accountAddress: String
    private val privateKey: String

    override val rpc: EthRpc
    override val explorer: EthExplorer

    private val confirmations: Int

    constructor(conf: Config) {
        config = conf

        accountAddress = config[EthConfig.Coin.accountAddress]
        privateKey = config[EthConfig.Coin.privateKey]

        val host = config[EthConfig.Connection.host]
        val port = config[EthConfig.Connection.port]
        val url = "http://$host:$port"
        rpc = EthRpc(url)

        confirmations = config[EthConfig.Coin.confirmations]

        val frequency = config[EthConfig.Explorer.frequency]
        explorer = EthExplorer(this, frequency)
    }

    constructor(ethRpc: EthRpc, ethExplorer: EthExplorer, configPath: String) {
        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(EthConfig)
            from.yaml.file(configFile)
        }
        config.validateRequired()

        accountAddress = config[EthConfig.Coin.accountAddress]
        privateKey = config[EthConfig.Coin.privateKey]

        confirmations = config[EthConfig.Coin.confirmations]

        rpc = ethRpc
        explorer = ethExplorer
    }

    /**
     * @return account balance in ether (15 signs).
     */
    override fun getBalance(): BigDecimal = rpc.getBalance(accountAddress)

    /**
     * @return account address.
     */
    override fun getAddress(): String = accountAddress

    override fun getTag(): Int? = Random.nextInt(0, Int.MAX_VALUE)

    /**
     * @param txid IxId object ( {hash : String, index : Int} )
     * @return Tx oject - generalized transaction template in the system
     */
    override fun getTx(txid: TxId): Tx {
        val trxTx = rpc.getTransactionByHash(txid.hash)
        return constructTx(trxTx)
    }

    /**
     * @param ethTx EthTx - ether transaction template
     * @return Tx oject - generalized transaction template in the system
     */
    fun constructTx(ethTx: EthTx): Tx {
        val latestBlock = rpc.getLatestBlock()

        return object : Tx {
            override fun currency() = Currency.ETH

            override fun hash() = ethTx.hash

            override fun amount() = hexToEth(ethTx.value)

            override fun destination() = ethTx.to

            override fun fee(): BigDecimal {
                return when {
                    this.status() == TxStatus.VALIDATING -> hexToWei(ethTx.gasPrice)
                        .multiply(hexToWei(ethTx.gas))
                    else -> hexToWei(ethTx.gasPrice)
                        .multiply(hexToWei(rpc.getTransactionReceipt(ethTx.hash).gasUsed))
                }
            }

            override fun status(): TxStatus {
                val conf = hexToBigInt(latestBlock.number) - hexToBigInt(ethTx.blockNumber)
                return when {
                    conf < confirmations.toBigInteger() -> TxStatus.VALIDATING
                    else -> TxStatus.CONFIRMED
                }
            }
        }
    }

    /**
     * @param amount amount of payment in ether
     * @param address address of the recipient
     * @return Tx oject - generalized transaction template in the system
     */
    override fun sendPayment(amount: BigDecimal, address: String, tag: Int?): Tx {
        rpc.unlockAccount(accountAddress, privateKey)
        val txHash = rpc.sendTransaction(__from = accountAddress, __to = address, __value = amount)
        return constructTx(rpc.getTransactionByHash(txHash))
    }

    fun getLatestBlock(): EthBlock {
        return rpc.getLatestBlock()
    }

    fun getBlockByHash(hash: String): EthBlock {
        return rpc.getBlockByHash(hash)
    }
}