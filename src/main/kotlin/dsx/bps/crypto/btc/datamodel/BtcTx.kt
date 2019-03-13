package dsx.bps.crypto.btc.datamodel

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class BtcTx(
    val amount: BigDecimal,
    val fee: BigDecimal,
    val confirmations: Int,
    val blockhash: String,
    @SerializedName("txid")
    val hash: String,
    val details: List<BtcTxDetail>,
    val hex: String
)