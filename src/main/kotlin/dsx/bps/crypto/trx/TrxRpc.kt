package dsx.bps.crypto.trx

import dsx.bps.crypto.trx.datamodel.TrxAccount
import dsx.bps.crypto.trx.datamodel.TrxBlock
import dsx.bps.crypto.trx.datamodel.TrxBroadcastTxResult
import dsx.bps.crypto.trx.datamodel.TrxTx
import dsx.bps.rpc.JsonRpcHttpClient
import dsx.bps.rpc.RpcRequest
import dsx.bps.rpc.RpcResponse
import java.math.BigDecimal
import java.net.URI
import java.net.URL
import java.util.*

class TrxRpc(fullNodeURL: String, solidityNodeURL: String): JsonRpcHttpClient(solidityNodeURL) {

    private val solURL: URL
    private val fulURL: URL
    private val solAuth: String?
    private val fulAuth: String?

    init {
        val sol = URL(solidityNodeURL)
        solURL = URI(sol.protocol, null, sol.host, sol.port, "/walletsolidity/", null, null).toURL()
        solAuth = sol.userInfo
            ?.toByteArray(charset)
            ?.let { Base64.getEncoder().encodeToString(it) }

        val ful = URL(fullNodeURL)
        fulURL = URI(ful.protocol, null, ful.host, ful.port, "/wallet/", null, null).toURL()
        fulAuth = ful.userInfo
            ?.toByteArray(charset)
            ?.let { Base64.getEncoder().encodeToString(it) }
    }

    fun getBalance(address: String, sol: Boolean = true): BigDecimal {
        val account = getAccount(address, sol)
        return account.balance
    }

    fun getAccount(address: String, sol: Boolean = true): TrxAccount {
        setNodeURL(sol)
        val result = query("getaccount", mapOf("address" to address))
        return gson.fromJson(result as String, TrxAccount::class.java)
    }

    fun getNowBlock(sol: Boolean = true): TrxBlock {
        setNodeURL(sol)
        val result = query("getnowblock") as String
        return gson.fromJson(result, TrxBlock::class.java)
    }

    fun getBlockByNum(num: Int, sol: Boolean = true): TrxBlock {
        setNodeURL(sol)
        val result = query("getblockbynum", mapOf("num" to num)) as String
        return gson.fromJson(result, TrxBlock::class.java)
    }

    fun getBlockById(hash: String): TrxBlock {
        setNodeURL(false)
        val result = query("getblockbyid", mapOf("value" to hash)) as String
        return gson.fromJson(result, TrxBlock::class.java)
    }

    fun getTransactionById(hash: String, sol: Boolean = true): TrxTx {
        setNodeURL(sol)
        val result = query("gettransactionbyid", mapOf("value" to hash)) as String
        return gson.fromJson(result, TrxTx::class.java)
    }

    fun createTransaction(toAddress: String, ownerAddress: String, amount: BigDecimal): TrxTx {
        setNodeURL(false)
        val result = query("createtransaction", mapOf(
            "amount" to amount,
            "to_address" to toAddress,
            "owner_address" to ownerAddress)
        ) as String
        return gson.fromJson(result, TrxTx::class.java)
    }

    fun getTransactionSign(privateKey: String, tx: TrxTx): TrxTx {
        // TODO: implement local tx sign
        setNodeURL(false)
        val result = query("gettransactionsign", mapOf("privateKey" to privateKey, "transaction" to tx)) as String
        return gson.fromJson(result, TrxTx::class.java)
    }

    fun broadcastTransaction(tx: TrxTx): TrxBroadcastTxResult {
        setNodeURL(false)
        val result = query("broadcasttransaction", tx) as String
        return gson.fromJson(result, TrxBroadcastTxResult::class.java)
    }

    private fun setNodeURL(sol: Boolean = true) {
        if (sol) {
            auth = solAuth
            rpcURL = solURL
        } else {
            auth = fulAuth
            rpcURL = fulURL
        }
    }

    override fun constructRequest(method: String, vararg params: Any): RpcRequest {
        val url = URL(rpcURL, method)
        var json = ""
        if (params.isNotEmpty()) {
            json = gson.toJson(params[0])
        }
        return RpcRequest(url, json)
    }

    override fun parseResponse(response: RpcResponse): Any? {
        return response.json
    }
}