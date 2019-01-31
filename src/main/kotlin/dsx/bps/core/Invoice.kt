package dsx.bps.core

import java.util.UUID
import java.math.BigDecimal

enum class InvoiceStatus {
    PAID,
    UNPAID
}

data class Invoice(val currency: Currency, val amount: BigDecimal, val address: String) {
    val id: String = UUID.randomUUID().toString()
    var status: InvoiceStatus = InvoiceStatus.UNPAID
        get() = if (received >= amount)
            InvoiceStatus.PAID
        else
            InvoiceStatus.UNPAID
    var received: BigDecimal = BigDecimal.ZERO
    val txIds: MutableList<String> = mutableListOf()
}