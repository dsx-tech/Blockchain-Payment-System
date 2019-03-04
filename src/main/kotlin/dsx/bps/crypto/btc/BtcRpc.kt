package dsx.bps.crypto.btc

import java.math.BigDecimal
import com.google.gson.reflect.TypeToken
import dsx.bps.rpc.JsonRpcHttpClient
import dsx.bps.crypto.btc.datamodel.*

class BtcRpc(url: String): JsonRpcHttpClient(url) {

    init {
        headers["Authorization"] = "Basic $auth"
    }

    fun getBalance(): BigDecimal {
        val result = query("getbalance")
        val json = gson.toJson(result)
        return gson.fromJson(json, BigDecimal::class.java)
    }

    fun getNewAddress(): String {
        val result = query("getnewaddress")
        return result as String
    }

    fun getBestBlockHash(): String {
        val result = query("getbestblockhash")
        return result as String
    }

    fun createRawTransaction(output: BtcTxOutput): String {
        val outs = mapOf(output.address to output.amount)
        val result = query("createrawtransaction", listOf<Any>(), listOf(outs))
        return result as String
    }

    fun fundRawTransaction(rawTx: String): BtcFundedRawTx {
        val result = query("fundrawtransaction", rawTx)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcFundedRawTx::class.java)
    }

    fun signRawTransactionWithWallet(rawTx: String): String {
        val result = query("signrawtransactionwithwallet", rawTx)
        val obj = gson.toJsonTree(result).asJsonObject
        if (obj["complete"].asBoolean) {
            return obj["hex"].asString
        } else {
            throw RuntimeException("Unable to sign raw transaction: $rawTx")
        }
    }

    fun sendRawTransaction(hex: String): String {
        val result = query("sendrawtransaction", hex)
        return result as String
    }

    fun getBlock(blockHash: String): BtcBlock {
        val result = query("getblock", blockHash)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcBlock::class.java)
    }

    fun listSinceBlock(hash: String): BtcListSinceBlock {
        val result = query("listsinceblock", hash)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcListSinceBlock::class.java)
    }

    fun getTransaction(txId: String): BtcTx {
        val result = query("gettransaction", txId)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcTx::class.java)
    }

    fun generate(n: Int): List<String> {
        val result = query("generate", n)
        val json = gson.toJson(result)
        return gson.fromJson(json, object: TypeToken<List<String>>() {}.type)
    }
}