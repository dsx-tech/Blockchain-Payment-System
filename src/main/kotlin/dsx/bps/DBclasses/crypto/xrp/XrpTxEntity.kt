package dsx.bps.DBclasses.crypto.xrp

import dsx.bps.DBclasses.core.tx.TxEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class XrpTxEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<XrpTxEntity>(XrpTxTable)

    var fee by XrpTxTable.fee
    var account by XrpTxTable.account
    var sequence by XrpTxTable.sequence
    var validated by XrpTxTable.validated
    var tx by TxEntity referencedOn XrpTxTable.txId
}