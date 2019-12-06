package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object PaymentTable: IntIdTable() {
    val status: Column<String> = varchar("status", 10)
    val fee: Column<String> = varchar("fee", 100)
    val paymentId: Column<String> = varchar("invoiceId", 100)
    val currency: Column<String> = varchar("currency", 100)
    val amount: Column<String> = varchar("amount", 100)
    val address: Column<String> = varchar("address", 100)
    val tag: Column<Int?> = integer("address").nullable()
    val payableId = reference("payableId", PayableTable)
}