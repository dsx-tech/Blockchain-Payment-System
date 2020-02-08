package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object BtcTxTable: IntIdTable() {
    val amount: Column<BigDecimal> = decimal("amount", 30, 15)
    val fee: Column<BigDecimal?> = decimal("fee", 30, 15).nullable()
    val confirmations: Column<Int> = integer("confirmations")
    val blockHash: Column<String> = varchar("blockHash", 500)
    val hash: Column<String> = varchar("hash", 500)
    val address: Column<String> = varchar("address", 100)
    val TxId = reference("TxId", TxTable)
}