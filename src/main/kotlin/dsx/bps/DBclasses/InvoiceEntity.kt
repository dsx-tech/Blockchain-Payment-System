package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class InvoiceEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<InvoiceEntity>(InvoiceTable)

    var status by InvoiceTable.status
    var received by InvoiceTable.received
    var invoiceId by InvoiceTable.invoiceId
    var currency by InvoiceTable.currency
    var amount by InvoiceTable.amount
    var address by InvoiceTable.address
    var tag by InvoiceTable.tag
    var payable by PayableEntity referencedOn InvoiceTable.payableId
}