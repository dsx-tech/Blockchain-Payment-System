package dsx.bps.crypto.eth.datamodel

import com.google.gson.annotations.SerializedName

data class EthTxInfo(
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("gas")
    val gas: String,
    @SerializedName("gasPrice")
    val gasPrice: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("data")
    val data: String
   // @SerializedName("nonce") //добавить потом когда нужна будет опция перезаписи транзакций
    // val nonce: String //а лучше добавить с теми же остальными полями но в другом классе
)