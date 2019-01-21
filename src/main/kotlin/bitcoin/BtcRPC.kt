package dsx.bps.kotlin.bitcoin

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

    fun getBlock(blockHash: String?, verbosity: Int): BtcJSON.BtcBlock? {
        if (blockHash == null)
            return null
        val json = gson.toJson(query("getblock", blockHash, verbosity))
        return Gson().fromJson(json, BtcJSON.BtcBlock::class.java)
    }

    fun getBlock(height: Int, verbosity: Int): BtcJSON.BtcBlock? {
        val hash = getBlockHash(height)
        return getBlock(hash, verbosity)
    }

    fun fundRawTransaction(rawTx: String): String {
        val result = query("fundrawtransaction", rawTx) as Map<String, *>
        return result["hex"] as String
    }

    fun signRawTransactionWithWallet(rawTx: String): String {
        val result = query("signrawtransactionwithwallet", rawTx) as Map<String, *>
        if (result["complete"] as Boolean) {
            return result["hex"] as String
        } else {
            throw RuntimeException("Unable to fund raw transaction: $rawTx")
        }
    }

    override fun listSinceBlock(hash: String): BtcJSON.BtcListSinceBlock {
        val json = gson.toJson(query("listsinceblock", hash))
        return gson.fromJson(json, BtcJSON.BtcListSinceBlock::class.java)
    }

    override fun getTransaction(txId: String?): BtcJSON.BtcTx {
        val json = gson.toJson(query("gettransaction", txId))
        return gson.fromJson(json, BtcJSON.BtcTx::class.java)
    }
}