package dsx.bps.crypto.eth

import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger


class EthRpc(url : String) : Connector  {

    private val web3j = Web3j.build(HttpService(url))
    private val basicGasLimit  = 90000

    fun getBalance(address: String): BigDecimal {
        val ethGetBalance : EthGetBalance = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest"))
            .sendAsync()
            .get()
        return Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER)

    }

    fun getTransactionByHash(hash: String) : Transaction{
        val transactionInfo = web3j.ethGetTransactionByHash(hash).sendAsync().get()
        return  transactionInfo.transaction.get()
    }


    fun getBlockByHash(hash: String) : org.web3j.protocol.core.methods.response.EthBlock.Block{
        val blockInfo = web3j.ethGetBlockByHash(hash, true).sendAsync().get()
        return  blockInfo.block
    }

    fun getLatestBlock(): org.web3j.protocol.core.methods.response.EthBlock.Block {
        val ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST,
            true).sendAsync().get()
        return ethBlock.block
    }

    fun getAllPendingTransactionsCount(): Int {
        val ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.PENDING,
            true).sendAsync().get()
        return ethBlock.block.transactions.size
    }

    fun getPendingTransactionsCount(address: String): Int {
        val ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.PENDING,
            true).sendAsync().get()
        val tx = ethBlock.block.transactions.map { it -> it.get() as org.web3j.protocol.core.methods.response.EthBlock.TransactionObject }
        return tx.filter { it -> it.from == address }.size
    }

    fun getAllTransactionsCount(address: String): BigInteger {
        val ethGetTransactionCount = web3j.ethGetTransactionCount(
            address, DefaultBlockParameterName.LATEST
        ).sendAsync().get()
        return ethGetTransactionCount.getTransactionCount() + getPendingTransactionsCount(address)
            .toBigInteger()
    }

    fun getTransactionReceiptByHash(hash: String) : TransactionReceipt{
        val receiptInfo = web3j.ethGetTransactionReceipt(hash).sendAsync().get()
        return  receiptInfo.transactionReceipt.get()
    }

    fun getGasPrice(): BigInteger {
        val result = web3j.ethGasPrice().sendAsync().get()
        return result.gasPrice
    }


    fun getTransactionCount(address: String): BigInteger {
        val ethGetTransactionCount = web3j.ethGetTransactionCount(
            address, DefaultBlockParameterName.LATEST
        ).sendAsync().get()
        return ethGetTransactionCount.transactionCount
    }

    fun generateWalletFile(password: String, pathToWallet: String) : String{
        val fileName = WalletUtils.generateNewWalletFile(
            password,
            File(pathToWallet)
        )
        val credentials = WalletUtils.loadCredentials(password, pathToWallet + "/" + fileName)
        return  credentials.address
    }

    fun createRawTransaction(nonce : BigInteger, gasPrice : BigInteger = getGasPrice(),
                             gasLimit : BigInteger = basicGasLimit.toBigInteger(), toAddress : String,
                             value : BigDecimal) : RawTransaction{
        val weiValue = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger()
        return RawTransaction.createEtherTransaction(
            nonce, gasPrice, gasLimit, toAddress, weiValue)
    }

    fun signTransaction(rawTransaction : RawTransaction, credentials : Credentials) : String{
        val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
        return Numeric.toHexString(signedMessage)
    }

    fun sendTransaction(hexValue : String) : String{
        val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get()
        return  ethSendTransaction.transactionHash
    }

    fun sendTransaction(pathToWallet : String, password: String, to : String, value: BigDecimal) : String{
        val credentials = WalletUtils.loadCredentials(password, pathToWallet)
        val transactionReceipt = Transfer.sendFunds(
            web3j, credentials, to,
            value, Convert.Unit.ETHER
        ).send()
        return transactionReceipt.transactionHash
    }

    fun waitForSomeBlocksMining()
    {
        val latestHash = getLatestBlock()
        var count = 0
        while (getLatestBlock() == latestHash && count < 160)
        {
            Thread.sleep(5000)
            count++
        }
        if (count >= 160)
        {
            throw Exception("Block mining timed out")
        }
    }
}