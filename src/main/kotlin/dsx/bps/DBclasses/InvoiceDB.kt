package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object Invoices: IntIdTable() {
    val status: Column<String> = varchar("status", 10)
    val received: Column<Int> = integer("received")
//    val btcTxs = reference("btctransactions", BtcTxs)
//    val trxTxs = reference("trxtransactions", TrxTxs)
//    val xrpTxs = reference("xrptransactions", XrpTxs)
}