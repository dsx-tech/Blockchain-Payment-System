package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object BtcTxTable: IntIdTable() {
    val confirmations: Column<Int> = integer("confirmations")
    val address: Column<String> = varchar("address", 100)
    val txId = reference("TxId", TxTable)
}