package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class XrpTx(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<XrpTx>(XrpTxs)

    var amount by XrpTxs.amount
    var fee by XrpTxs.fee
    var hash by XrpTxs.hash
    //var hex by XrpTxs.hex
    var account by XrpTxs.account
    var destination by XrpTxs.destination
    var sequence by XrpTxs.sequence
    //var type by XrpTxs.type
    var invoiceIndex by XrpTxs.invoiceIndex
    var invoice by Invoice optionalReferencedOn BtcTxs.invoice
    var payment by Payment optionalReferencedOn BtcTxs.payment
}