package dsx.bps.kotlin.core

import java.math.BigDecimal
import java.util.*

enum class InvoiceStatus {
    PAID,
    UNPAID
}

data class Invoice(val currency: Currency, val amount: BigDecimal, val address: String) {
    val id: UUID = UUID.randomUUID()
    var status: InvoiceStatus = InvoiceStatus.UNPAID
    var received: BigDecimal = BigDecimal.ZERO
    val txIds: MutableList<String> = mutableListOf()
}