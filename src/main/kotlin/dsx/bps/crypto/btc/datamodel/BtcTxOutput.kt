package dsx.bps.crypto.btc.datamodel

import java.math.BigDecimal

data class BtcTxOutput(
    val address: String,
    val amount: BigDecimal
)
