package dsx.bps.crypto.btc.datamodel

import java.math.BigDecimal

data class BtcTx(
    val amount: BigDecimal,
    val fee: BigDecimal,
    val confirmations: Int,
    val blockhash: String,
    val txid: String,
    val details: List<BtcTxDetail>,
    val hex: String
)