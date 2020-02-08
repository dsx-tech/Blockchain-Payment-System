package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object TrxTxTable: IntIdTable() {
    val amount: Column<BigDecimal> = decimal("amount", 30, 15)
    val hash: Column<String> = varchar("hash", 500)
    val address: Column<String> = varchar("address", 100)
    val contractRet: Column<String> = varchar("contractRet", 100)
    val TxId = reference("TxId", TxTable)
}