package dsx.bps.crypto.btc.datamodel

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class BtcTxDetail(
    val address: String,
    val category: String,
    val amount: BigDecimal,
    val vout: Int,
    @SerializedName("fee")
    private val _fee: BigDecimal?
) {
    val fee: BigDecimal
        get() = _fee ?: BigDecimal.ZERO
}