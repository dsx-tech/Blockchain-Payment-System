package dsx.bps.DBclasses

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.TxStatus
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object TxTable: IntIdTable() {
    val status = enumeration("status", TxStatus::class)
    val destination: Column<String> = varchar("destination", 100)
    val tag: Column<String?> = varchar("tag", 100).nullable()
    val amount: Column<BigDecimal> = decimal("amount", 30, 15)
    val fee: Column<BigDecimal> = decimal("fee", 30, 15)
    val hash: Column<String> = varchar("hash", 500)
    val index: Column<Long> = long("index")
    val currency = enumeration("currency", Currency::class)
    val payableId = reference("payableId", PayableTable).nullable()
}