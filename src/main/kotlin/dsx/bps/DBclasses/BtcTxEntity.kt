package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class BtcTxEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<BtcTxEntity>(BtcTxTable)

    var fee by BtcTxTable.fee
    var confirmations by BtcTxTable.confirmations
    var blockHash by BtcTxTable.blockHash
    var address by BtcTxTable.address
    var Tx by TxEntity referencedOn BtcTxTable.TxId
}