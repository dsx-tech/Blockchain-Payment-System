package dsx.bps.DBclasses.core

import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.core.tx.TxTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class CryptoAddressEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<CryptoAddressEntity>(CryptoAddressTable)

    var type by CryptoAddressTable.type
    var address by CryptoAddressTable.address
    var currency by CryptoAddressTable.currency
    val txs by TxEntity optionalReferrersOn TxTable.payableId
}