package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class XrpTxEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<XrpTxEntity>(XrpTxTable)

    var amount by XrpTxTable.amount
    var fee by XrpTxTable.fee
    var hash by XrpTxTable.hash
    var account by XrpTxTable.account
    var destination by XrpTxTable.destination
    var sequence by XrpTxTable.sequence
    var validated by XrpTxTable.validated
    var Tx by TxEntity referencedOn XrpTxTable.TxId
}