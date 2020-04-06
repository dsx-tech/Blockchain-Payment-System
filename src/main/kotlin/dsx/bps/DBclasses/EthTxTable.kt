package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object EthTxTable: IntIdTable() {
    val address: Column<String> = varchar("address", 100)
    val nonce: Column<Long> = long("nonce")
    val txId = reference("TxId", TxTable)
}