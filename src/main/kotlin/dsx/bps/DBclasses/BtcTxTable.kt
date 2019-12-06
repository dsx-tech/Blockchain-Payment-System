package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object BtcTxTable: IntIdTable() {
    val amount: Column<String> = varchar("amount", 100)
    val fee: Column<String?> = varchar("fee", 100).nullable()
    val confirmations: Column<Int> = integer("confirmations")
    val blockHash: Column<String> = varchar("blockHash", 500)
    val hash: Column<String> = varchar("hash", 500)
    val address: Column<String> = varchar("adress", 100)
    val TxId = reference("TxId", TxTable)
}