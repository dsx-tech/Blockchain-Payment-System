package dsx.bps.crypto.trx.datamodel

import com.google.gson.annotations.SerializedName

data class TrxBlockRawData(
    val number: Int,
    @SerializedName("parentHash")
    val parentHash: String,
    val timestamp: Long,
    @SerializedName("txTrieRoot")
    val txTrieRoot: String,
    val version: Int,
    @SerializedName("witness_address")
    val witnessAddress: String
)