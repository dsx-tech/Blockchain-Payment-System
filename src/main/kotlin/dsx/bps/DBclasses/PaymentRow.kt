package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Payment(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Payment>(Payments)

    var status by Payments.status
    var fee by Payments.fee
//    var btcTx by BtcTx referencedOn Payments.btcTx
//    var trxTxs by TrxTx referencedOn Payments.trxTx
//    var xrpTxs by XrpTx referencedOn Payments.xrpTx
    val btcTxs by BtcTx optionalReferencedOn BtcTxs.payment
    val trxTxs by TrxTx optionalReferencedOn TrxTxs.payment
    val xrpTxs by BtcTx optionalReferencedOn XrpTxs.payment
}