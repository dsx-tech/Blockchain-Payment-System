package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class XrpTx(
    val hash: String,
    var validated: Boolean = false,
    @SerializedName("Account")
    val account: String,
    @SerializedName("Amount")
    val amount: BigDecimal,
    @SerializedName("Destination")
    val destination: String,
    @SerializedName("Fee")
    val fee: String,
    @SerializedName("Sequence")
    val sequence: Int,
    @SerializedName("TransactionType")
    val type: String,
    @SerializedName("DestinationTag")
    val destinationTag: Int? = null
) {
    lateinit var hex: String
}