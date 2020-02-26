package dsx.bps.DBservices

import java.math.BigDecimal

class DatabaseCreation {
    private val invService = InvoiceService()
    private val paeService = PaymentService()

    fun createInvoices() {
        invService.add("UNPAID", BigDecimal.ZERO, "inv1", "BTC", BigDecimal.ONE, "addr1", null)
        invService.add("UNPAID", BigDecimal.ZERO, "inv2", "BTC", BigDecimal.ONE, "addr2", null)
    }

    fun createPayments() {
        paeService.add("PENDING", "pay1", "BTC", BigDecimal.ONE, "addr1", null)
    }
}