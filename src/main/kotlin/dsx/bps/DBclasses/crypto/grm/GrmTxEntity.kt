package dsx.bps.DBclasses.crypto.grm

import dsx.bps.DBclasses.core.tx.TxEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class GrmTxEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GrmTxEntity>(GrmTxTable)

    var utime by GrmTxTable.utime
    var lt by GrmTxTable.lt
    var inMsg by GrmInMsgEntity referencedOn GrmTxTable.inMsg
    var tx by TxEntity referencedOn GrmTxTable.txId
}
