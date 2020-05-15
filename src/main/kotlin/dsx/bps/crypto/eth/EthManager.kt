package dsx.bps.crypto.eth

import com.uchuhimo.konf.Config
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.EthService
import dsx.bps.config.currencies.EthConfig
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.eth.ethereum.datamodel.EthAccount
import org.web3j.crypto.WalletUtils
import java.math.BigDecimal
import java.math.BigInteger

abstract class EthManager: Coin {

    override val config: Config
    val scanningCount : Int
    protected val systemAccount: EthAccount
    protected val defaultPasswordForNewAddresses: String
    protected val walletsDir: String
    protected val commonConnector: CommonConnector

    protected val url: String

    protected val frequency: Long
    internal val confirmations: Int

    internal val ethService: EthService
    internal val txService: TxService


    constructor(conf: Config, datasource: Datasource, txServ: TxService) {
        ethService = EthService(datasource)
        txService = txServ

        config = conf

        scanningCount = config[EthConfig.Coin.scanningCount]
        systemAccount = EthAccount(config[EthConfig.Coin.accountAddress],
            config[EthConfig.Coin.pathToWallet],
            config[EthConfig.Coin.password])
        defaultPasswordForNewAddresses = config[EthConfig.Coin.defaultPasswordForNewAddresses]
        walletsDir = config[EthConfig.Coin.walletsDir]

        val host = config[EthConfig.Connection.host]
        val port = config[EthConfig.Connection.port]
        url = "http://$host:$port"

        commonConnector = CommonConnector(url)
        confirmations = config[EthConfig.Coin.confirmations]
        frequency = config[EthConfig.Explorer.frequency]
    }

    fun getSystemAddress() = systemAccount.address

    fun getGasPrice(): BigInteger {
        return commonConnector.getGasPrice()
    }

    fun send(account: EthAccount = systemAccount, to: String, amount : BigDecimal, gasLimit: BigInteger = commonConnector.basicGasLimit ) : String {
        var nonce = ethService.getLatestNonce(account.address)
        if (nonce == null)
        {
            nonce = commonConnector.getTransactionCount(account.address)
        }
        else
        {
            nonce ++
        }

        val rawTransaction = commonConnector.createRawTransaction(nonce, toAddress = to, value = amount,
            gasLimit = gasLimit)
        val credentials = WalletUtils.loadCredentials(account.password, account.wallet)
        val signedTransaction = commonConnector.signTransaction(rawTransaction, credentials)
        return commonConnector.sendTransaction(signedTransaction)
    }

    @Deprecated("only for tests")
    override fun clearDb() {
        this.ethService.delete()
    }

}