package dsx.bps.crypto.trx

import dsx.bps.crypto.trx.datamodel.TrxAccount
import dsx.bps.crypto.trx.datamodel.TrxBlock
import dsx.bps.crypto.trx.datamodel.TrxBroadcastTxResult
import dsx.bps.crypto.trx.datamodel.TrxTx
import dsx.bps.crypto.xrp.datamodel.TrxTxInfo
import dsx.bps.rpc.JsonRpcHttpClient
import dsx.bps.rpc.RpcRequest
import dsx.bps.rpc.RpcResponse
import java.math.BigDecimal
import java.net.URL

class TrxRpc(url: String): JsonRpcHttpClient(url) {

    fun getBalance(address: String): BigDecimal {
        val account = getAccount(address)
        return account.balance
    }

    fun getAccount(address: String): TrxAccount {
        val result = query("getaccount", mapOf("address" to address))
        return gson.fromJson(result as String, TrxAccount::class.java)
    }

    fun getNowBlock(): TrxBlock {
        val result = query("getnowblock") as String
        return gson.fromJson(result, TrxBlock::class.java)
    }

    fun getBlockByNum(num: Int): TrxBlock {
        val result = query("getblockbynum", mapOf("num" to num)) as String
        return gson.fromJson(result, TrxBlock::class.java)
    }

    fun getBlockById(hash: String): TrxBlock {
        val result = query("getblockbyid", mapOf("value" to hash)) as String
        return gson.fromJson(result, TrxBlock::class.java)
    }

    fun getTransactionById(hash: String): TrxTx {
        val result = query("gettransactionbyid", mapOf("value" to hash)) as String
        return gson.fromJson(result, TrxTx::class.java)
    }

    fun getTransactionInfoById(hash: String): TrxTxInfo {
        val result = query("gettransactioninfobyid", mapOf("value" to hash)) as String
        return gson.fromJson(result, TrxTxInfo::class.java)
    }

    fun createTransaction(toAddress: String, ownerAddress: String, amount: BigDecimal): TrxTx {
        val result = query(
            "createtransaction", mapOf(
                "amount" to amount,
                "to_address" to toAddress,
                "owner_address" to ownerAddress
            )
        ) as String
        return gson.fromJson(result, TrxTx::class.java)
    }

    fun getTransactionSign(privateKey: String, tx: TrxTx): TrxTx {
        // TODO: implement local tx sign
        val result = query("gettransactionsign", mapOf("privateKey" to privateKey, "transaction" to tx)) as String
        return gson.fromJson(result, TrxTx::class.java)
    }

    fun broadcastTransaction(tx: TrxTx): TrxBroadcastTxResult {
        val result = query("broadcasttransaction", tx) as String
        return gson.fromJson(result, TrxBroadcastTxResult::class.java)
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