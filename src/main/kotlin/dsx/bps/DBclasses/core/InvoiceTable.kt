package dsx.bps.DBclasses.core

import dsx.bps.core.datamodel.InvoiceStatus
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object InvoiceTable: IntIdTable() {
    val status = enumeration("status", InvoiceStatus::class)
    val received: Column<BigDecimal> = decimal("received", 30, 15)
    val invoiceId: Column<String> = varchar("invoiceId", 100)
    val amount: Column<BigDecimal> = decimal("amount", 30, 15)
    val tag: Column<String?> = varchar("tag", TableConstants.tagMaxLength).nullable()
    val cryptoAddressId = reference("cryptoAddressId", CryptoAddressTable)
}