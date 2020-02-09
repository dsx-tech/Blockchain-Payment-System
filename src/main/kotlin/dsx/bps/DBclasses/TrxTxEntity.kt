package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class TrxTxEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<TrxTxEntity>(TrxTxTable)

    var amount by TrxTxTable.amount
    var address by TrxTxTable.address
    var contractRet by TrxTxTable.contractRet
    var Tx by TxEntity referencedOn TrxTxTable.TxId
}