package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object InvoiceTable: IntIdTable() {
    val status: Column<String> = varchar("status", 10)
    val received: Column<BigDecimal> = decimal("received", 30, 15)
    val invoiceId: Column<String> = varchar("invoiceId", 100)
    val currency: Column<String> = varchar("currency", 100)
    val amount: Column<BigDecimal> = decimal("amount", 30, 15)
    val address: Column<String> = varchar("address", 100)
    val tag: Column<Int?> = integer("tag").nullable()
    val payableId = reference("payableId", PayableTable)
}