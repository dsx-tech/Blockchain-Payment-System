package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class XrpTx(
    val hash: String,
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
    val destinationTag: Int? = null,
    @SerializedName("ledger_index")
    val ledgerIndex: Long,
    val meta: XrpTxMeta? = null,
    val validated: Boolean = false
) {
    lateinit var hex: String
}