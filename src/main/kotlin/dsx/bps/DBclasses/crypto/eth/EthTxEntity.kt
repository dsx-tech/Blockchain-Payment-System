package dsx.bps.DBclasses.crypto.eth

import dsx.bps.DBclasses.core.tx.TxEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class EthTxEntity (id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<EthTxEntity>(EthTxTable)
    var address by EthTxTable.address
    var nonce by EthTxTable.nonce
    var tx by TxEntity referencedOn EthTxTable.txId
}