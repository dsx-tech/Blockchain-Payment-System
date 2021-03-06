package dsx.bps.DBclasses.core.tx

import dsx.bps.DBclasses.core.CryptoAddressEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class TxEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<TxEntity>(TxTable)

    var status by TxTable.status
    var tag by TxTable.tag
    var amount by TxTable.amount
    var fee by TxTable.fee
    var hash by TxTable.hash
    var index by TxTable.index
    var cryptoAddress by CryptoAddressEntity referencedOn TxTable.cryptoAddressId
}