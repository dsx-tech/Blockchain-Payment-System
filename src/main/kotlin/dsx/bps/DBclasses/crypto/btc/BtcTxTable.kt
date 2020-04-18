package dsx.bps.DBclasses.crypto.btc

import dsx.bps.DBclasses.core.TxTable
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object BtcTxTable: IntIdTable() {
    val confirmations: Column<Int> = integer("confirmations")
    val address: Column<String> = varchar("address", 100)
    val txId = reference("TxId", TxTable)
}