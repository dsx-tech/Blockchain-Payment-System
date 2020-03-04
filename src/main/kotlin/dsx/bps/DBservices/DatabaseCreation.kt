package dsx.bps.DBservices

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.PaymentStatus
import java.math.BigDecimal

class DatabaseCreation(datasource: Datasource) {
    private val invService = InvoiceService(datasource)
    private val paeService = PaymentService(datasource)

    fun createInvoices() {
        invService.add("UNPAID", BigDecimal.ZERO, "inv1", Currency.BTC, BigDecimal.ONE, "addr1", null)
        invService.add("UNPAID", BigDecimal.ZERO, "inv2", Currency.BTC, BigDecimal.ONE, "addr2", null)
    }

    fun createPayments() {
        paeService.add(PaymentStatus.PENDING, "pay1", Currency.BTC, BigDecimal.ONE, "addr1", null)
    }
}