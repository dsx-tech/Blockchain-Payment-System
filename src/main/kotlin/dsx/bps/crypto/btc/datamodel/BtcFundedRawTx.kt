package dsx.bps.crypto.btc.datamodel

import java.math.BigDecimal

data class BtcFundedRawTx(
    val fee: BigDecimal,
    val hex: String
)