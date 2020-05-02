package dsx.bps.crypto.eth

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.EthService
import dsx.bps.config.currencies.EthConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.eth.datamodel.EthAccount
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.methods.response.EthBlock.Block
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.utils.Convert
import java.io.File
import java.math.BigDecimal

class EthCoin: Coin {
    override val currency = Currency.ETH
    override val config: Config

    val scanningCount : Int
    internal val accountAddress: String
    private val password: String
    private val pathToWallet: String
    private val defaultPasswordForNewAddresses: String
    private val walletsDir: String

    override val connector: EthRpc
    private val ethRouter: EthRouter
    override val explorer: EthExplorer

    internal val confirmations: Int

    internal val ethService: EthService
    internal val txService: TxService


    constructor(conf: Config, datasource: Datasource, txServ: TxService) {
        ethService = EthService(datasource)
        txService = txServ

        config = conf

        scanningCount = config[EthConfig.Coin.scanningCount]
        accountAddress = config[EthConfig.Coin.accountAddress]
        password = config[EthConfig.Coin.password]
        pathToWallet = config[EthConfig.Coin.pathToWallet]
        defaultPasswordForNewAddresses = config[EthConfig.Coin.defaultPasswordForNewAddresses]
        walletsDir = config[EthConfig.Coin.walletsDir]

        val host = config[EthConfig.Connection.host]
        val port = config[EthConfig.Connection.port]
        val url = "http://$host:$port"
        connector = EthRpc(url)

        confirmations = config[EthConfig.Coin.confirmations]
        val frequency = config[EthConfig.Explorer.frequency]

        val microCoin = EthMicroCoin(connector, accountAddress, confirmations, ethService, txService)
        ethRouter = EthRouter(microCoin)
        explorer = EthExplorer(this, frequency, datasource, txService, ethRouter)
    }

    constructor(ethRpc: EthRpc, ethExplorer: EthExplorer, configPath: String, datasource: Datasource, txServ: TxService) {
        ethService = EthService(datasource)
        txService = txServ

        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(EthConfig)
            from.yaml.file(configFile)
        }
        config.validateRequired()

        scanningCount = config[EthConfig.Coin.scanningCount]
        accountAddress = config[EthConfig.Coin.accountAddress]
        password = config[EthConfig.Coin.password]
        pathToWallet = config[EthConfig.Coin.pathToWallet]
        defaultPasswordForNewAddresses = config[EthConfig.Coin.defaultPasswordForNewAddresses]
        walletsDir = config[EthConfig.Coin.walletsDir]

        connector = ethRpc
        explorer = ethExplorer

        confirmations = config[EthConfig.Coin.confirmations]

        val microCoin = EthMicroCoin(connector, accountAddress, confirmations, ethService, txService)
        ethRouter = EthRouter(microCoin)
    }

    /**
     * @return account balance in ether.
     */
    override fun getBalance(): BigDecimal = connector.getBalance(accountAddress)

    /**
     * Get new smart-contract address. This is an alternative to getting an address by creating a new wallet.
     * @return smart-contract address.
     */
    fun getSmartAddress(): String{
        return connector.generateSmartWallet(pathToWallet, password).address
    }

    /**
     * @return new account address
     */
    override fun getAddress() : String {
        val newAccount = connector.generateWalletFile(defaultPasswordForNewAddresses, walletsDir)
        ethRouter.addAccount(newAccount)
        return newAccount.address
    }

    /**
     * @param txid TxId object ( {hash : String, index : Int} )
     * @return Tx oject - generalized transaction template in the system
     */
    override fun getTx(txid: TxId): Tx {
        val ethTx = connector.getTransactionByHash(txid.hash)
        return constructTx(ethTx)
    }

    /**
     * @param ethTx EthTx - ether transaction template
     * @return Tx oject - generalized transaction template in the system
     */
    fun constructTx(ethTx: Transaction): Tx {
        return object: Tx {
            override fun currency() = Currency.ETH

            override fun hash() = ethTx.hash

            override fun amount() = Convert.fromWei(ethTx.value.toBigDecimal(), Convert.Unit.ETHER)

            override fun destination() = ethTx.to

            override fun fee(): BigDecimal {
                return if (this.status() == TxStatus.VALIDATING) {
                    Convert.fromWei(ethTx.gasPrice.multiply(ethTx.gas).toBigDecimal(), Convert.Unit.ETHER)
                } else {
                    Convert.fromWei(ethTx.gasPrice
                        .multiply(connector.getTransactionReceiptByHash(ethTx.hash).gasUsed).toBigDecimal(),
                        Convert.Unit.ETHER)
                }
            }

            override fun status(): TxStatus {
                val latestBlock = connector.getLatestBlock()
                if (ethTx.blockHash == null) {
                    return TxStatus.VALIDATING
                } else {
                    val conf = latestBlock.number - ethTx.blockNumber
                    if (conf < confirmations.toBigInteger()) {
                        return TxStatus.VALIDATING
                    } else {
                        return TxStatus.CONFIRMED
                    }
                }
            }
        }
    }

    /**
     * @param amount amount of payment in ether
     * @param address address of the recipient
     * @return Tx oject - generalized transaction template in the system
     */
    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
        return send(EthAccount(accountAddress, pathToWallet, password), address, amount)
    }

    fun getLatestBlock(): Block {
        return connector.getLatestBlock()
    }

    fun getBlockByHash(hash: String): Block {
        return connector.getBlockByHash(hash)
    }

    @Deprecated("only for tests")
    override fun kill(){
        this.explorer.kill()
    }

    @Deprecated("only for tests")
    override fun clearDb() {
        this.ethService.delete()
    }

    fun send(account: EthAccount, to: String, amount : BigDecimal = connector.getBalance(account.address)) : Tx {
        var nonce = ethService.getLatestNonce(account.address)
        if (nonce == null)
        {
            nonce = connector.getTransactionCount(account.address)
        }
        else
        {
            nonce ++
        }

        val rawTransaction = connector.createRawTransaction(nonce, toAddress = to, value = amount)
        val credentials = WalletUtils.loadCredentials(account.password, account.wallet)
        val signedTransaction = connector.signTransaction(rawTransaction, credentials)
        val resultHash = connector.sendTransaction(signedTransaction)
        val transaction = constructTx(connector.getTransactionByHash(resultHash))

        val txs = txService.add(transaction.status(), transaction.destination(),"",
            transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())
        ethService.add(account.address, nonce.toLong(), txs)
        return constructTx(connector.getTransactionByHash(resultHash))
    }

}