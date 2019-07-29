package dsx.bps.crypto.trx.datamodel

import com.google.gson.annotations.SerializedName

data class TrxTxParameter(
    @SerializedName("type_url")
    val typeUrl: String,
    val value: TrxTxValue
)
