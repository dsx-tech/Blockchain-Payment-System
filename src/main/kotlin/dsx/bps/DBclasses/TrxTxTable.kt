package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object TrxTxTable: IntIdTable() {
    val address: Column<String> = varchar("address", 100)
    val txId = reference("TxId", TxTable)
}

object ContractRetTable: IntIdTable() {
    val contractRet: Column<String> = varchar("contractRet", 100)
    val trxTable = reference("TrxTable", TrxTxTable)
}