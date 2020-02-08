package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class TxEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<TxEntity>(TxTable)

    var status by TxTable.status
    var currency by TxTable.currency
    var payable by PayableEntity referencedOn TxTable.payableId
}