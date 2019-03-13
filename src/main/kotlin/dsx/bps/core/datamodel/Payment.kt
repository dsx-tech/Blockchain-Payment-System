package dsx.bps.core.datamodel

import java.util.UUID
import java.math.BigDecimal

data class Payment(val currency: Currency, val amount: BigDecimal, val address: String, val tag: Int? = null) {
    val id: String = UUID.randomUUID().toString()
    var status: PaymentStatus = PaymentStatus.UNCONFIRMED
    var index: Int = -1
    lateinit var tx: String
    lateinit var hex: String
    lateinit var fee: BigDecimal
}