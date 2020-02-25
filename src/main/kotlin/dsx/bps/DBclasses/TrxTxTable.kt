package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object TrxTxTable: IntIdTable() {
    val address: Column<String> = varchar("address", 100)
    val TxId = reference("TxId", TxTable)
}

object ContractRetTable: IntIdTable() {
    val contractRet: Column<String> = varchar("contractRet", 100)
    val trxTable = reference("TrxTable", TrxTxTable)
}