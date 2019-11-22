package dsx.bps.crypto.xrp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dsx.bps.crypto.xrp.datamodel.*
import dsx.bps.exception.rpc.xrp.XrpRpcException
import dsx.bps.rpc.JsonRpcHttpClient
import dsx.bps.rpc.RpcResponse
import java.math.BigDecimal

class XrpRpc(url: String): JsonRpcHttpClient(url) {

    override val gson: Gson = GsonBuilder()
        .registerTypeAdapter(
            XrpAmount::class.java,
            XrpAmount.Companion.XrpAmountDeserializer()
        )
        .create()

    fun getBalance(account: String): BigDecimal {
        val accountData = getAccountInfo(account)
        return BigDecimal(accountData.balance)
    }

    fun getTransaction(hash: String): XrpTx {
        val params = mapOf("transaction" to hash)
        val result = query("tx", params)
        val json = gson.toJson(result)
        return gson.fromJson(json, XrpTx::class.java)
    }

    fun getLastLedger(tx: Boolean = false, validated: Boolean = true): XrpLedger {
        val params = mutableMapOf<String, Any>("transactions" to tx)
        if (validated)
            params["ledger_index"] = "validated"
        val result = query("ledger", params)
        val obj = gson.toJsonTree(result).asJsonObject
        return gson.fromJson(obj["ledger"], XrpLedger::class.java)
    }

    fun getLedger(ledgerHash: String, tx: Boolean = false, validated: Boolean = true): XrpLedger {
        val params = mutableMapOf("ledger_hash" to ledgerHash, "transactions" to tx)
        if (validated)
            params["ledger_index"] = "validated"
        val result = query("ledger", params)
        val obj = gson.toJsonTree(result).asJsonObject
        return gson.fromJson(obj["ledger"], XrpLedger::class.java)
    }

    fun getAccountInfo(account: String, validated: Boolean = true): XrpAccountData {
        val params = mutableMapOf("account" to account)
        if (validated) {
            params["ledger_index"] = "validated"
        }
        val result  = query("account_info", params)
        val obj = gson.toJsonTree(result).asJsonObject
        return gson.fromJson(obj["account_data"], XrpAccountData::class.java)
    }

    fun getServerInfo(): XrpServerInfo {
        val result = query("server_info")
        val obj = gson.toJsonTree(result).asJsonObject
        return gson.fromJson(obj["info"], XrpServerInfo::class.java)
    }

    fun getAccountTxs(account: String, indexMin: Long, indexMax: Long): XrpAccountTxs {
        val params =  mapOf(
            "account" to account,
            "ledger_index_min" to indexMin,
            "ledger_index_max" to indexMax
        )
        val result = query("account_tx", params)
        val json = gson.toJson(result)
        return gson.fromJson(json, XrpAccountTxs::class.java)
    }

    fun sign(privateKey: String, tx: XrpTxPayment, offline: Boolean = true): String {
        // TODO: implement local tx sign
        val params = mutableMapOf(
            "secret" to privateKey,
            "tx_json" to tx,
            "offline" to offline)
        val result = query("sign", params)
        val obj = gson.toJsonTree(result).asJsonObject
        return obj["tx_blob"].asString
    }

    fun submit(hex: String): XrpTx {
        val params = mapOf("tx_blob" to hex)
        val result = query("submit", params)
        val obj = gson.toJsonTree(result).asJsonObject

        val engineResult = obj["engine_result"].asString
        if (engineResult != "tesSUCCESS" && !engineResult.startsWith("tec"))
            throw XrpRpcException("engine_result: ${obj["engine_result"].asString}\n" +
                    "engine_result_code: ${obj["engine_result_code"].asInt}\n" +
                    "engine_result_message: ${obj["engine_result_message"].asString}")

        val tx: XrpTx = gson.fromJson(obj["tx_json"], XrpTx::class.java)
        tx.hex = obj["tx_blob"].asString
        return tx
    }

    fun getSequence(account: String): Int {
        val accountData = getAccountInfo(account, false)
        return accountData.sequence
    }

    fun getTxCost(): BigDecimal {
        val info = getServerInfo()
        val loadFactor = BigDecimal(info.loadFactor)
        val baseFee = info.validatedLedger.baseFeeXrp
        val drops = (baseFee * loadFactor * BigDecimal(1e6)).longValueExact()
        return BigDecimal(drops)
    }

    fun ledgerAccept(): Long {
        val result = query("ledger_accept")
        val obj = gson.toJsonTree(result).asJsonObject
        return obj["ledger_current_index"].asLong
    }

    override fun parseResponse(response: RpcResponse): Any? {
        val obj = response.json
            .let { gson.fromJson(it, Map::class.java) }
            .let { gson.toJsonTree(it["result"]).asJsonObject }

        if (obj["status"].asString == "error") {
            val code = obj["error_code"].asInt
            val error = obj["error"].asString
            val message = obj["error_message"].asString
            throw XrpRpcException("RPC error \"$error\": code $code, message: \"$message\"," +
                    "\n for response: ${response.json}")
        }

        return obj
    }
}