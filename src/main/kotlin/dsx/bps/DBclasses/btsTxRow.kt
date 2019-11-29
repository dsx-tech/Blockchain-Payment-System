package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class BtcTx(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<BtcTx>(BtcTxs)

    var amount by BtcTxs.amount
    var fee by BtcTxs.fee
    var confirmations by BtcTxs.confirmations
    var blockHash by BtcTxs.blockHash
    var hash by BtcTxs.hash
    //var hex by BtcTxs.hex
    var adress by BtcTxs.adress
    var invoiceIndex by BtcTxs.invoiceIndex
    var invoice by Invoice optionalReferencedOn BtcTxs.invoice
    var payment by Payment optionalReferencedOn BtcTxs.payment
}