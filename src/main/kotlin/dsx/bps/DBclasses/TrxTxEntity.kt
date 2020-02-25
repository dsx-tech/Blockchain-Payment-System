package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class TrxTxEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<TrxTxEntity>(TrxTxTable)

    var address by TrxTxTable.address
    val contractRets by ContractRetEntity referrersOn ContractRetTable.trxTable
    var Tx by TxEntity referencedOn TrxTxTable.TxId
}

class ContractRetEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ContractRetEntity>(ContractRetTable)

    var contractRet by ContractRetTable.contractRet
    var trx by TrxTxEntity referencedOn ContractRetTable.trxTable
}