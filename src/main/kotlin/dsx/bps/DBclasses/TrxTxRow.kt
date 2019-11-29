package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class TrxTx(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<TrxTx>(TrxTxs)

    var amount by TrxTxs.amount
    var hash by TrxTxs.hash
    var hex by TrxTxs.hex
    //var signature by TrxTxs.signature
    var adress by TrxTxs.adress
    var contractRet by TrxTxs.contractRet
    var invoiceIndex by TrxTxs.invoiceIndex
    var invoice by Invoice optionalReferencedOn BtcTxs.invoice
    var payment by Payment optionalReferencedOn BtcTxs.payment
}