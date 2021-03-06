package dsx.bps.crypto.btc

import com.google.gson.reflect.TypeToken
import dsx.bps.connector.rpc.JsonRpcHttpClient
import dsx.bps.crypto.btc.datamodel.BtcBlock
import dsx.bps.crypto.btc.datamodel.BtcListSinceBlock
import dsx.bps.crypto.btc.datamodel.BtcTx
import dsx.bps.exception.connector.btc.BtcRpcException
import java.math.BigDecimal

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
            throw BtcRpcException("Unable to sign raw transaction: $hex")
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

    fun generatetoaddress(n: Int, address: String): List<String> {
        val result = query("generatetoaddress", n, address)
        val json = gson.toJson(result)
        return gson.fromJson(json, object: TypeToken<List<String>>() {}.type)
    }
}