package dsx.bps.DBclasses.grm

import dsx.bps.DBclasses.TxEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class GrmTxEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GrmTxEntity>(GrmTxTable)

    var utime by GrmTxTable.utime
    var lt by GrmTxTable.lt
    var tx by TxEntity referencedOn GrmTxTable.txId
}
