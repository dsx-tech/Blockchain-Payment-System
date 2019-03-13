package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class XrpTxPayment(
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
    @SerializedName("DestinationTag")
    val destinationTag: Int? = null,
    @SerializedName("TransactionType")
    val type: String = "Payment"
)