package dsx.bps.crypto.trx.datamodel

import com.google.gson.annotations.SerializedName

data class TrxBroadcastTxResult(
    @SerializedName("result")
    private val _result: Boolean? = null,
    val code: String?,
    val message: String?
) {
    val success: Boolean
        get() = _result ?: false
}