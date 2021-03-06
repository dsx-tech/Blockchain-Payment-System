package dsx.bps.crypto.eth

import dsx.bps.connector.Connector
import dsx.bps.crypto.eth.ethereum.datamodel.EthAccount
import dsx.bps.crypto.eth.ethereum.datamodel.Proxy
import dsx.bps.crypto.eth.ethereum.datamodel.SmartContract
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthBlock
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger

class CommonConnector(url: String) : Connector() {
    val web3j = Web3j.build(HttpService(url))
    val basicGasLimit = BigInteger("90000")

    fun getBalance(address: String): BigDecimal {
        val ethGetBalance: EthGetBalance = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest"))
            .send()
        return Convert.fromWei(ethGetBalance.balance.toString(), Convert.Unit.ETHER)

    }

    fun getTransactionByHash(hash: String): Transaction {
        val transactionInfo = web3j.ethGetTransactionByHash(hash).send()
        return transactionInfo.transaction.get()
    }

    fun getBlockByHash(hash: String): EthBlock.Block {
        val blockInfo = web3j.ethGetBlockByHash(hash, true).send()
        return blockInfo.block
    }

    fun getLatestBlock(): EthBlock.Block {
        val ethBlock = web3j.ethGetBlockByNumber(
            DefaultBlockParameterName.LATEST,
            true
        ).send()
        return ethBlock.block
    }

    fun getAllPendingTransactionsCount(): Int {
        val ethBlock = web3j.ethGetBlockByNumber(
            DefaultBlockParameterName.PENDING,
            true
        ).send()
        return ethBlock.block.transactions.size
    }

    fun getPendingTransactionsCount(address: String): BigInteger {

        return web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send().transactionCount
    }

    fun getAllTransactionsCount(address: String): BigInteger {
        val ethGetTransactionCount = web3j.ethGetTransactionCount(
            address, DefaultBlockParameterName.LATEST
        ).send()
        return ethGetTransactionCount.transactionCount + getPendingTransactionsCount(address)
    }

    fun getTransactionReceiptByHash(hash: String): TransactionReceipt {
        val receiptInfo = web3j.ethGetTransactionReceipt(hash).send()
        return receiptInfo.transactionReceipt.get()
    }

    fun getGasPrice(): BigInteger {
        val result = web3j.ethGasPrice().send()
        return result.gasPrice
    }

    fun getTransactionCount(address: String): BigInteger {
        val ethGetTransactionCount = web3j.ethGetTransactionCount(
            address, DefaultBlockParameterName.LATEST
        ).send()
        return ethGetTransactionCount.transactionCount
    }

    fun generateWalletFile(password: String, pathToWallet: String): EthAccount {
        val fileName = WalletUtils.generateNewWalletFile(
            password,
            File(pathToWallet)
        )
        val credentials = WalletUtils.loadCredentials(password, "$pathToWallet/$fileName")
        return EthAccount(credentials.address, "$pathToWallet/$fileName", password)
    }

    fun generateSmartWallet(pathToWallet: String, password: String): SmartContract {
        val credentials = WalletUtils.loadCredentials(password, pathToWallet)
        val contract = Proxy.deploy(web3j, credentials, DefaultGasProvider()).send()
        val fee = Convert.fromWei(this.getGasPrice()
            .multiply(contract.transactionReceipt.get().gasUsed).toString(), Convert.Unit.ETHER)
        return SmartContract(contract.contractAddress, fee)
    }

    fun createRawTransaction(
        nonce: BigInteger, gasPrice: BigInteger = getGasPrice(),
        gasLimit: BigInteger = basicGasLimit, toAddress: String,
        value: BigDecimal
    ): RawTransaction {
        val weiValue = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger()
        return RawTransaction.createEtherTransaction(
            nonce, gasPrice, gasLimit, toAddress, weiValue
        )
    }

    fun signTransaction(rawTransaction: RawTransaction, credentials: Credentials): String {
        val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
        return Numeric.toHexString(signedMessage)
    }

    fun sendTransaction(hexValue: String): String {
        val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send()
        return ethSendTransaction.transactionHash
    }

    fun sendTransaction(pathToWallet: String, password: String, to: String, value: BigDecimal): String {
        val credentials = WalletUtils.loadCredentials(password, pathToWallet)
        val transactionReceipt = Transfer.sendFunds(
            web3j, credentials, to,
            value, Convert.Unit.ETHER
        ).send()
        return transactionReceipt.transactionHash
    }

    fun getBLockByNumber(number : BigInteger) : EthBlock.Block{
        return web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(number), true).send().block
    }
}