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

    fun createRawTransaction(amount: BigDecimal, address: String): String {
        val outs = mapOf(address to amount)
        val result = query("createrawtransaction", listOf<Any>(), listOf(outs))
        return result as String
    }

    fun fundRawTransaction(hex: String): String {
        val result = query("fundrawtransaction", hex)
        val obj = gson.toJsonTree(result).asJsonObject
        return obj["hex"].asString
    }

    fun signRawTransactionWithWallet(hex: String): String {
        val result = query("signrawtransactionwithwallet", hex)
        val obj = gson.toJsonTree(result).asJsonObject
        if (obj["complete"].asBoolean) {
            return obj["hex"].asString
        } else {
            throw RuntimeException("Unable to sign raw transaction: $hex")
        }
    }

    fun sendRawTransaction(hex: String): String {
        val result = query("sendrawtransaction", hex)
        return result as String
    }

    fun getBlock(hash: String): BtcBlock {
        val result = query("getblock", hash)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcBlock::class.java)
    }

    fun listSinceBlock(hash: String): BtcListSinceBlock {
        val result = query("listsinceblock", hash)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcListSinceBlock::class.java)
    }

    fun getTransaction(hash: String): BtcTx {
        val result = query("gettransaction", hash)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcTx::class.java)
    }

    fun generate(n: Int): List<String> {
        val result = query("generate", n)
        val json = gson.toJson(result)
        return gson.fromJson(json, object: TypeToken<List<String>>() {}.type)
    }
}