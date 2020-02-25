package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object XrpTxTable: IntIdTable() {
    val fee: Column<BigDecimal> = decimal("fee", 30, 15)
    val account: Column<String> = varchar("account", 50)
    val sequence: Column<Int> = integer("sequence")
    val validated: Column<Boolean?> = bool("validated").nullable()
    val TxId = reference("TxId", TxTable)
}