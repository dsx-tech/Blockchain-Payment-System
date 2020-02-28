package dsx.bps.crypto.trx.datamodel

import com.google.gson.annotations.SerializedName

data class TrxTx(
    @SerializedName("txID")
    val hash: String,
    @SerializedName("raw_data")
    val rawData: TrxTxRawData,
    @SerializedName("raw_data_hex")
    val hex: String,
    @SerializedName("signature")
    private val _signature: List<String>?,
    @SerializedName("ret")
    private val _ret: List<TrxTxRet>?
) {

    val ret: List<TrxTxRet>
        get() = _ret ?: listOf()

    val signature: List<String>
        get() = _signature ?: listOf()
}