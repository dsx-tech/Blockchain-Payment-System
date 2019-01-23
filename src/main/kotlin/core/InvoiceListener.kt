package dsx.bps.kotlin.core

import java.util.*
import kotlin.collections.HashMap

abstract class InvoiceListener: Observer {

    protected val unpaidInvoices: HashMap<UUID, Invoice> = HashMap()

    fun addInvoice(inv: Invoice) {
        unpaidInvoices.putIfAbsent(inv.id, inv)
    }
}