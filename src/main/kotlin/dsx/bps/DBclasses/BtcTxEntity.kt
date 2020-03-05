package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class BtcTxEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<BtcTxEntity>(BtcTxTable)

    var confirmations by BtcTxTable.confirmations
    var address by BtcTxTable.address
    var tx by TxEntity referencedOn BtcTxTable.txId
}