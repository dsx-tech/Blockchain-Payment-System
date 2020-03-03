package dsx.bps.crypto.trx.datamodel

import com.google.gson.annotations.SerializedName

data class TrxTxInfo(
    val blockNumber: Int,
    val blockTimeStamp: Long,
    val contractResult: List<String>,
    @SerializedName("id")
    val hash: String
)