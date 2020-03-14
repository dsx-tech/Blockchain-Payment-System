package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class PayableEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<PayableEntity>(PayableTable)

    var type by PayableTable.type
    val txs by TxEntity optionalReferrersOn TxTable.payableId
}