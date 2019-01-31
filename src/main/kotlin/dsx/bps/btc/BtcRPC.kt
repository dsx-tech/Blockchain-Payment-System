package dsx.bps.btc

import com.google.gson.Gson
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient

class BtcRPC: BitcoinJSONRPCClient {

    private val gson = Gson()

    constructor(): super(BitcoinJSONRPCClient.DEFAULT_JSONRPC_REGTEST_URL)

    constructor(url: String): super(url)

    fun connect(url: String) {
        addNode(url, "onetry")
    }

    fun disconnect(url: String) {
        query("disconnectnode", url)
    }

    override fun getBlock(blockHash: String): BtcJSON.BtcBlock {
        val result = query("getblock", blockHash)
        val json = gson.toJson(result)
        return Gson().fromJson(json, BtcJSON.BtcBlock::class.java)
    }

    override fun getBlock(height: Int): BtcJSON.BtcBlock {
        val hash = getBlockHash(height)
        return getBlock(hash)
    }

    fun fundRawTransaction(rawTx: String): String {
        val result = query("fundrawtransaction", rawTx)
        val obj = gson.toJsonTree(result).asJsonObject
        return obj.get("hex").asString
    }

    fun signRawTransactionWithWallet(rawTx: String): String {
        val result = query("signrawtransactionwithwallet", rawTx)
        val obj = gson.toJsonTree(result).asJsonObject
        if (obj.get("complete").asBoolean) {
            return obj.get("hex").asString
        } else {
            throw RuntimeException("Unable to fund raw transaction: $rawTx")
        }
    }

    override fun listSinceBlock(hash: String): BtcJSON.BtcListSinceBlock {
        val result = query("listsinceblock", hash)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcJSON.BtcListSinceBlock::class.java)
    }

    override fun getTransaction(txId: String?): BtcJSON.BtcTx {
        val result = query("gettransaction", txId)
        val json = gson.toJson(result)
        return gson.fromJson(json, BtcJSON.BtcTx::class.java)
    }
}