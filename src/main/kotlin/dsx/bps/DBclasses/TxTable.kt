package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object TxTable: IntIdTable() {
    val status: Column<String> = varchar("status", 15)
    val hash: Column<String> = varchar("hash", 500)
    val index: Column<Int> = integer("index")
    val currency: Column<String> = varchar("type", 15)
    val payableId = reference("payableId", PayableTable).nullable()
}