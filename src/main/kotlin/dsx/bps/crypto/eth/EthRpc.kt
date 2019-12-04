package dsx.bps.crypto.eth

import dsx.bps.crypto.eth.datamodel.EthBlock
import dsx.bps.crypto.eth.datamodel.EthTx
import dsx.bps.crypto.eth.datamodel.EthTxInfo
import dsx.bps.crypto.eth.datamodel.EthTxReceipt
import dsx.bps.rpc.JsonRpcHttpClient
import java.math.BigDecimal
import java.math.BigInteger


class EthRpc(url: String) : JsonRpcHttpClient(url) {

    /**
     * @param address string id of account
     * @return account balance in ether (15 signs)
     */
    fun getBalance(address: String): BigDecimal {
        val result = query("eth_getBalance", address, "latest").toString()
        return hexToEth(result)
    }

    fun getTransactionByHash(hash: String): EthTx {
        val result = query("eth_getTransactionByHash", hash).toString()
        return gson.fromJson(result, EthTx::class.java)
    }

    fun getBlockByHash(hash: String): EthBlock {
        val result = query("eth_getBlockByHash", hash, true).toString()
        return gson.fromJson(result, EthBlock::class.java)
    }

    fun getLatestBlock(): EthBlock {
        val result = query("eth_getBlockByNumber", "latest", true).toString()
        return gson.fromJson(result, EthBlock::class.java)
    }

    fun getTransactionReceipt(hash: String): EthTxReceipt {
        val result = query("eth_getTransactionReceipt", hash).toString()
        return gson.fromJson(result, EthTxReceipt::class.java)
    }

    /**
     * @return account balance in Wei
     */
    fun getGasPrice(): BigInteger {
        val result = query("eth_gasPrice").toString()
        return hexToBigInt(result)
    }

    fun getTransactionCount(address: String): BigInteger {
        val result = query("eth_getTransactionCount", address, "latest").toString()
        return hexToBigInt(result)
    }

    fun unlockAccount(address: String, password: String): Boolean {
        return query("personal_unlockAccount", address, password, 3600).toString().toBoolean()
    }

    /**
     * @param __value amount of payment in ether
     * @return account balance in Wei
     */
    fun sendTransaction(
        __from: String, __to: String, __gas: BigInteger = 90000.toBigInteger()
        , __gasPrice: BigInteger = getGasPrice(), __value: BigDecimal, __data: String = "",
        __nonce: BigInteger = getTransactionCount(__from)
    ): String {

        val txInfo = EthTxInfo(
            __from, __to, bigIntToHex(__gas), bigIntToHex(__gasPrice),
            bigIntToHex(__value.multiply(Math.pow(10.0, 18.0).toBigDecimal()).toBigInteger()),
            __data
        )

        return query("eth_sendTransaction", txInfo).toString()
    }
}