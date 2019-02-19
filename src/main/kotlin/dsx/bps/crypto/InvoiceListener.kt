package dsx.bps.crypto

import dsx.bps.core.Invoice
import java.util.Observer

abstract class InvoiceListener: Observer {

    protected val unpaidInvoices: HashMap<String, Invoice> = HashMap()

    fun addInvoice(inv: Invoice) {
        unpaidInvoices.putIfAbsent(inv.id, inv)
    }
}