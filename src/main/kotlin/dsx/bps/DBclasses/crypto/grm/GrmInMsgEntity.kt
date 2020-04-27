package dsx.bps.DBclasses.crypto.grm

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class GrmInMsgEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GrmInMsgEntity>(GrmInMsgTable)

    var sourceAddress by GrmInMsgTable.sourceAddress
    var destination by GrmInMsgTable.destination
    var value by GrmInMsgTable.value
    var fwdFee by GrmInMsgTable.fwdFee
    var ihrFee by GrmInMsgTable.ihrFee
    var createdLt by GrmInMsgTable.createdLt
    var bodyHash by GrmInMsgTable.bodyHash
    var msgText by GrmInMsgTable.msgText
}