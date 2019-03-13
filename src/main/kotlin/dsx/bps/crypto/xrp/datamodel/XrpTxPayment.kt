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
    val sequence: Long,
    @SerializedName("TransactionType")
    val type: String = "Payment",
    @SerializedName("DestinationTag")
    val destinationTag: Long? = null
)