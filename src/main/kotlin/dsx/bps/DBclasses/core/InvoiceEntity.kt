package dsx.bps.DBclasses.core

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class InvoiceEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<InvoiceEntity>(InvoiceTable)

    var status by InvoiceTable.status
    var received by InvoiceTable.received
    var invoiceId by InvoiceTable.invoiceId
    var amount by InvoiceTable.amount
    var tag by InvoiceTable.tag
    var cryptoAddress by CryptoAddressEntity referencedOn InvoiceTable.cryptoAddressId
}