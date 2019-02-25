package dsx.bps.crypto.btc.datamodel

import java.math.BigDecimal

data class BtcTxDetail(
    val address: String,
    val amount: BigDecimal,
    val category: String
)