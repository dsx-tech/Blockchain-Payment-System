package dsx.bps.DBclasses.crypto.grm

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class GrmOutMsgEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GrmOutMsgEntity>(GrmOutMsgTable)

    var sourceAddress by GrmOutMsgTable.sourceAddress
    var destination by GrmOutMsgTable.destination
    var value by GrmOutMsgTable.value
    var fwdFee by GrmOutMsgTable.fwdFee
    var ihrFee by GrmOutMsgTable.ihrFee
    var createdLt by GrmOutMsgTable.createdLt
    var bodyHash by GrmOutMsgTable.bodyHash
    var msgText by GrmOutMsgTable.msgText
    var grmTx by GrmTxEntity referencedOn GrmOutMsgTable.grmTx
}