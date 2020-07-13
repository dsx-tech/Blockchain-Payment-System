package dsx.bps.core

import dsx.bps.DBservices.core.InvoiceService
import dsx.bps.DBservices.core.PaymentService
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.PaymentStatus
import java.math.BigDecimal

class DatabaseCreation {
    private val invService = InvoiceService()
    private val payService = PaymentService()

    fun createInvoices() {
        invService.add(InvoiceStatus.UNPAID, BigDecimal.ZERO, "inv1", Currency.BTC, BigDecimal.ONE, "addr1", null)
        invService.add(InvoiceStatus.UNPAID, BigDecimal.ZERO, "inv2", Currency.BTC, BigDecimal.ONE, "addr2", null)
    }

    fun createPayments() {
        payService.add(PaymentStatus.PENDING, "paym1", Currency.BTC, BigDecimal.ONE, "PaymAddress", null)
    }
}