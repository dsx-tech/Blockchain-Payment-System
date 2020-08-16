package dsx.bps.crypto.eth.erc20.datamodel

import java.math.BigDecimal

data class Erc20Tx(
    val hash: String,
    val to: String,
    val amount: BigDecimal
)