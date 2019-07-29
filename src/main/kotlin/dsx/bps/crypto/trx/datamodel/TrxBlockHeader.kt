package dsx.bps.crypto.trx.datamodel

import com.google.gson.annotations.SerializedName

data class TrxBlockHeader(
    @SerializedName("raw_data")
    val rawData: TrxBlockRawData,
    @SerializedName("witness_signature")
    val witnessSignature: String
)