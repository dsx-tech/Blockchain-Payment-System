package dsx.bps.DBclasses.core

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.PaymentStatus
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object PaymentTable: IntIdTable() {
    val status = enumeration("status", PaymentStatus::class)
    val fee: Column<BigDecimal?> = decimal("fee", 30, 15).nullable()
    val paymentId: Column<String> = varchar("paymentId", 100)
    val currency = enumeration("currency", Currency::class)
    val amount: Column<BigDecimal> = decimal("amount", 30, 15)
    val address: Column<String> = varchar("address", TableConstants.addressMaxLength)
    val tag: Column<String?> = varchar("tag", TableConstants.tagMaxLength).nullable()
    val payableId = reference("payableId", PayableTable)
}