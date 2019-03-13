package dsx.bps.crypto.xrp.datamodel

import java.math.BigDecimal
import com.google.gson.annotations.SerializedName
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx

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
    val sequence: Int,
    @SerializedName("TransactionType")
    val type: String,
    @SerializedName("DestinationTag")
    val destinationTag: Int? = null
): Tx {

    lateinit var hex: String

    override fun currency() = Currency.XRP

    override fun hash() = hash

    override fun index() = sequence

    override fun amount() = amount

    override fun destination() = destination

    override fun tag() = destinationTag

    override fun fee() = BigDecimal(fee)

    override fun confirmations() = if (validated) 1 else 0
}