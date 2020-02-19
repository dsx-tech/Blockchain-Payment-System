package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object TxTable: IntIdTable() {
    val status: Column<String> = varchar("status", 15)
    val destination: Column<String> = varchar("destination", 100)
    val tag: Column<Int?> = integer("tag").nullable()
    val amount: Column<BigDecimal> = decimal("amount", 30, 15)
    val fee: Column<BigDecimal> = decimal("fee", 30, 15)
    val hash: Column<String> = varchar("hash", 500)
    val index: Column<Int> = integer("index")
    val currency: Column<String> = varchar("currency", 15)
    val payableId = reference("payableId", PayableTable).nullable()
}