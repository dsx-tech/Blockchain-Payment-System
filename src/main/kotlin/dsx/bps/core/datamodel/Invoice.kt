package dsx.bps.core.datamodel

import java.math.BigDecimal

data class Invoice(val id: String, val currency: Currency, val amount: BigDecimal, val address: String, val tag: Int? = null) {
    val status: InvoiceStatus
        get() = if (received >= amount)
            InvoiceStatus.PAID
        else
            InvoiceStatus.UNPAID
    var received: BigDecimal = BigDecimal.ZERO
    val txids: MutableList<TxId> = mutableListOf()
}