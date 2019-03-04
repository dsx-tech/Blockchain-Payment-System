package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class XrpValidatedLedger(
    val age: Long,
    val hash: String,
    val seq: Long,
    @SerializedName("base_fee_xrp")
    val baseFeeXrp: BigDecimal,
    @SerializedName("reserve_base_xrp")
    val reserveBaseXrp: BigDecimal,
    @SerializedName("reserve_inc_xrp")
    val reserveIncXrp: BigDecimal
)