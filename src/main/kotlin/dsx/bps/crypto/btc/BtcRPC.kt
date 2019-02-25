package dsx.bps.crypto.btc

import com.google.gson.Gson
import dsx.bps.crypto.btc.datamodel.*
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient

class BtcRPC(url: String) : BitcoinJSONRPCClient(url) {

    private val gson = Gson()

    override fun getBlock(blockHash: String): BtcBlock {
        val result = query("getblock", blockHash)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcBlock::class.java)
    }

    override fun getBlock(height: Int): BtcBlock {
        val hash = getBlockHash(height)
        return getBlock(hash)
    }

    fun fundRawTransaction(rawTx: String): BtcFundedRawTx {
        val result = query("fundrawtransaction", rawTx)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcFundedRawTx::class.java)
    }

    fun signRawTransactionWithWallet(rawTx: String): String {
        val result = query("signrawtransactionwithwallet", rawTx)
        val obj = gson.toJsonTree(result).asJsonObject
        if (obj.get("complete").asBoolean) {
            return obj.get("hex").asString
        } else {
            throw RuntimeException("Unable to sign raw transaction: $rawTx")
        }
    }

    override fun listSinceBlock(hash: String): BtcListSinceBlock {
        val result = query("listsinceblock", hash)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcListSinceBlock::class.java)
    }

    override fun getTransaction(txId: String?): BtcTx {
        val result = query("gettransaction", txId)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcTx::class.java)
    }
}