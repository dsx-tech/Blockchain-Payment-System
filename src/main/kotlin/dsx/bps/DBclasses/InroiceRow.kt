package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Invoice(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Invoice>(Invoices)

    var status by Invoices.status
    var received by Invoices.received// кол во адресс валюта
//    var btsTxs by listOf<BtcTx referencedOn Invoices.btcTxs>()
//    var trxTxs by TrxTx referencedOn Invoices.trxTxs
//    var xrpTxs by XrpTx referencedOn Invoices.xrpTxs
    val btcTxs by BtcTx optionalReferencedOn BtcTxs.invoice
    val trxTxs by TrxTx optionalReferencedOn TrxTxs.invoice
    val xrpTxs by BtcTx optionalReferencedOn XrpTxs.invoice
}