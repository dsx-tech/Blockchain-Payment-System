package dsx.bps.crypto.trx.datamodel

import java.math.BigDecimal

data class TrxAccount(
    val address: String,
    val balance: BigDecimal
)