package dsx.bps.core.datamodel

import java.util.UUID
import java.math.BigDecimal

data class Invoice(val currency: Currency, val amount: BigDecimal, val address: String, val tag: Int? = null) {
    val id: String = UUID.randomUUID().toString()
    val status: InvoiceStatus
        get() = if (received >= amount)
            InvoiceStatus.PAID
        else
            InvoiceStatus.UNPAID
    var received: BigDecimal = BigDecimal.ZERO
    val txids: MutableList<TxId> = mutableListOf()
}