package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName

data class XrpTxMeta(
    @SerializedName("TransactionIndex")
    val transactionIndex: Int,
    @SerializedName("TransactionResult")
    val transactionResult: String,
    @SerializedName("delivered_amount")
    val deliveredAmount: XrpAmount?
)
