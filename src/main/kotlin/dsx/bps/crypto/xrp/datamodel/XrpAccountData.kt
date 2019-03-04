package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName

data class XrpAccountData(
    @SerializedName("Account")
    val account: String,
    @SerializedName("Balance")
    val balance: String,
    @SerializedName("Sequence")
    val sequence: Long
)