package dsx.bps.crypto.btc.datamodel

import java.math.BigDecimal

data class BtcTxDetail(
    val address: String,
    val category: String,
    val amount: BigDecimal,
    val fee: BigDecimal
)