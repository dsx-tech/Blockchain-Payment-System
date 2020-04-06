package dsx.bps.DBclasses.grm

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class GrmLastTxPayDestAccEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GrmLastTxPayDestAccEntity>(GrmLastTxPayDestAccTable)

    var destAccountAddress by GrmLastTxPayDestAccTable.destAccountAddress
    var paymentTxLt by GrmLastTxPayDestAccTable.paymentTxLt
    var paymentTxHash by GrmLastTxPayDestAccTable.paymentTxHash
    var lastProcTxLt by GrmLastTxPayDestAccTable.lastProcTxLt
    var lastProcTxHash by GrmLastTxPayDestAccTable.lastProcTxHash
}