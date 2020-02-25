package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName

data class XrpTx(
    val hash: String,
    @SerializedName("Account")
    val account: String,
    @SerializedName("Amount")
    val amount: XrpAmount?,
    @SerializedName("Destination")
    private val _destination: String?,
    @SerializedName("Fee")
    val fee: String,
    @SerializedName("Sequence")
    val sequence: Int,
    @SerializedName("TransactionType")
    val type: String,
    @SerializedName("DestinationTag")
    val destinationTag: Int?,
    @SerializedName("ledger_index")
    val ledgerIndex: Long,
    val meta: XrpTxMeta?,
    @SerializedName("validated")
    private val _validated: Boolean?
) {

    lateinit var hex: String

    val destination: String
        get() = _destination ?: ""

    val validated: Boolean
        get() = _validated ?: false
}