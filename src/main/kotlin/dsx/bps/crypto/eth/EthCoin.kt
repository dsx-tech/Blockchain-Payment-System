package dsx.bps.crypto.eth

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.EthConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.eth.datamodel.EthTxStandart
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.methods.response.Transaction
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger


class EthCoin : Coin {
    override val currency = Currency.ETH
    override val config: Config

    private val accountAddress: String
    private val password: String
    private val pathToWallet: String
    private val defaultPasswordForNewAddresses : String
    private  val walletsDir : String

    override val rpc: EthRpc
    override val explorer: EthExplorer

    private val confirmations: Int
    private var nonce : BigInteger

    constructor(conf: Config) {
        config = conf

        accountAddress = config[EthConfig.Coin.accountAddress]
        password = config[EthConfig.Coin.password]
        pathToWallet = config[EthConfig.Coin.pathToWallet]
        defaultPasswordForNewAddresses = config[EthConfig.Coin.defaultPasswordForNewAddresses]
        walletsDir = config[EthConfig.Coin.walletsDir]

        val host = config[EthConfig.Connection.host]
        val port = config[EthConfig.Connection.port]
        val url = "http://$host:$port"
        rpc = EthRpc(url)

        confirmations = config[EthConfig.Coin.confirmations]
        nonce = rpc.getTransactionCount(accountAddress)

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
        password = config[EthConfig.Coin.password]
        pathToWallet = config[EthConfig.Coin.pathToWallet]
        defaultPasswordForNewAddresses = config[EthConfig.Coin.defaultPasswordForNewAddresses]
        walletsDir = config[EthConfig.Coin.walletsDir]

        rpc = ethRpc
        explorer = ethExplorer

        confirmations = config[EthConfig.Coin.confirmations]
        nonce = rpc.getTransactionCount(accountAddress)
    }

    /**
     * @return account balance in ether (15 signs).
     */
    override fun getBalance(): BigDecimal = rpc.getBalance(accountAddress)

    /**
     * @return account address.
     */
    override fun getAddress() : String = rpc.generateWalletFile(defaultPasswordForNewAddresses, walletsDir)
        // как и где надо хранить пароли???

    /**
     * @param txid IxId object ( {hash : String, index : Int} )
     * @return Tx oject - generalized transaction template in the system
     */
    override fun getTx(txid: TxId): Tx {
        val ethTx = rpc.getTransactionByHash(txid.hash)
        return constructTx(ethTx)
    }

    /**
     * @param ethTx EthTx - ether transaction template
     * @return Tx oject - generalized transaction template in the system
     */
    fun constructTx(ethTx: Transaction): Tx {
        return EthTxStandart(ethTx, rpc,confirmations)
    }

    /**
     * @param amount amount of payment in ether
     * @param address address of the recipient
     * @return Tx oject - generalized transaction template in the system
     */
    override fun sendPayment(amount: BigDecimal, address: String, tag: Int?): Tx {
        val rawTransaction = rpc.createRawTransaction(nonce, toAddress = address, value = amount)
        val credentials = WalletUtils.loadCredentials(password, pathToWallet)
        val signedTransaction = rpc.signTransaction(rawTransaction, credentials)
        val resultHash = rpc.sendTransaction(signedTransaction)
        nonce ++ // разработать безопасную работу с nonce
        return constructTx(rpc.getTransactionByHash(resultHash))
    }

    fun getLatestBlock(): org.web3j.protocol.core.methods.response.EthBlock.Block {
        return rpc.getLatestBlock()
    }

    fun getBlockByHash(hash: String): org.web3j.protocol.core.methods.response.EthBlock.Block {
        return rpc.getBlockByHash(hash)
    }
}