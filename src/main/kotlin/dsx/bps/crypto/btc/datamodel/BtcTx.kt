package dsx.bps.crypto.btc.datamodel

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class BtcTx(
    val amount: BigDecimal,
    @SerializedName("fee")
    private val _fee: BigDecimal?,
    val confirmations: Int,
    val blockhash: String,
    @SerializedName("txid")
    val hash: String,
    val details: List<BtcTxDetail>,
    val hex: String
) {
    val fee: BigDecimal
        get() = _fee ?: BigDecimal.ZERO
}