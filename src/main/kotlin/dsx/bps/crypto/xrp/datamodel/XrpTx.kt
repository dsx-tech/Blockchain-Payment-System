package dsx.bps.crypto.xrp.datamodel

import java.math.BigDecimal
import com.google.gson.annotations.SerializedName
import dsx.bps.core.Currency
import dsx.bps.crypto.common.Tx

data class XrpTx(
    val hash: String,
    var validated: Boolean,
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
    val type: String,
    @SerializedName("DestinationTag")
    val destinationTag: Long? = null
): Tx {

    override fun currency() = Currency.XRP

    override fun destination() = destination

    override fun amount() = amount

    override fun fee() = BigDecimal(fee)

    override fun confirmations() = if (validated) 1 else 0

    override fun hash() = hash

    override fun tag() = destinationTag?.toString() ?: ""
}