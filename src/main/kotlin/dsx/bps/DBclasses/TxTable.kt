package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object TxTable: IntIdTable() {
    val status: Column<String> = varchar("status", 15)
    val currency: Column<String> = varchar("type", 15)
    val payableId = reference("payableId", PayableTable)
}