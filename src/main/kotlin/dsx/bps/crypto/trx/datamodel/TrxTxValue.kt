package dsx.bps.crypto.trx.datamodel

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class TrxTxValue(
    val amount: BigDecimal,
    @SerializedName("owner_address")
    val ownerAddress: String,
    @SerializedName("to_address")
    val toAddress: String
)