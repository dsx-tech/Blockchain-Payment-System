package dsx.bps.crypto.eth.datamodel

import com.google.gson.annotations.SerializedName

data class EthBlock(
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("extraData")
    val extraData: String,
    @SerializedName("gasLimit")
    val gasLimit: String,
    @SerializedName("hash")
    val hash: String,
    @SerializedName("logsBloom")
    val logsBloom: String,
    @SerializedName("miner")
    val miner: String,
    @SerializedName("mixHash")
    val mixHash: String,
    @SerializedName("nonce")
    val nonce: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("parentHash")
    val parentHash: String,
    @SerializedName("receiptsRoot")
    val receiptsRoot: String,
    @SerializedName("sha3Uncles")
    val sha3Uncles: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("stateRoot")
    val stateRoot: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("totalDifficulty")
    val totalDifficulty: String,
    @SerializedName("transactions")
    val transactions: List<EthTx>,
    @SerializedName("transactionsRoot")
    val transactionsRoot: String,
    @SerializedName("uncles")
    val uncles: List<EthBlock>?
)
