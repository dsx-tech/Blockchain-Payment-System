package dsx.bps.core.datamodel

import java.util.UUID
import java.math.BigDecimal

data class Payment(val currency: Currency, val amount: BigDecimal, val address: String) {
    val id: String = UUID.randomUUID().toString()
    lateinit var txId: String
    lateinit var fee: BigDecimal
    lateinit var rawTx: String
}