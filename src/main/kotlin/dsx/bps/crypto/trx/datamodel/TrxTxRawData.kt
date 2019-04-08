package dsx.bps.crypto.trx.datamodel

import com.google.gson.annotations.SerializedName

data class TrxTxRawData(
    val contract: List<TrxTxContract>,
    @SerializedName("ref_block_bytes")
    val refBlockBytes: String,
    @SerializedName("ref_block_hash")
    val refBlockHash: String,
    val expiration: Long,
    val timestamp: Long
)