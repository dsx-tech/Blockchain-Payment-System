package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object BtcTxTable: IntIdTable() {
    val confirmations: Column<Int> = integer("confirmations")
    val blockHash: Column<String> = varchar("blockHash", 500)
    val address: Column<String> = varchar("address", 100)
    val TxId = reference("TxId", TxTable)
}