package dsx.bps.core

import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.InvoiceService
import dsx.bps.DBservices.PaymentService
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.PaymentStatus
import java.math.BigDecimal

class DatabaseCreation(datasource: Datasource) {
    private val invService = InvoiceService(datasource)
    private val payService = PaymentService(datasource)

    fun createInvoices() {
        invService.add(InvoiceStatus.UNPAID, BigDecimal.ZERO, "inv1", Currency.BTC, BigDecimal.ONE, "addr1", null)
        invService.add(InvoiceStatus.UNPAID, BigDecimal.ZERO, "inv2", Currency.BTC, BigDecimal.ONE, "addr2", null)
    }

    fun createPayments() {
        payService.add(PaymentStatus.PENDING, "pay1", Currency.BTC, BigDecimal.ONE, "addr1", null)
    }
}