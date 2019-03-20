package dsx.bps.crypto.btc.datamodel

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class BtcTxSinceBlock(
    val address: String,
    val category: String,
    val amount: BigDecimal,
    val fee: BigDecimal,
    val confirmations: Int,
    @SerializedName("txid")
    val hash: String,
    val vout: Int
)