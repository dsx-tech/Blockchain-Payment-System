package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object TrxTxTable: IntIdTable() {
    val amount: Column<String> = varchar("amoout", 100)
    val hash: Column<String> = varchar("hash", 500)
    val address: Column<String> = varchar("adress", 100)
    val contractRet: Column<String> = varchar("contractRet", 100)
    val TxId = reference("TxId", TxTable)
}