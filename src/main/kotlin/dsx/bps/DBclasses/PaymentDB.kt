package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object Payments: IntIdTable() {
    val status: Column<String> = varchar("status", 10)
    val fee: Column<Int> = integer("fee")
//    val btcTx = reference("btctransaction", BtcTxs)
//    val trxTx = reference("trxtransaction", TrxTxs)
//    val xrpTx = reference("xrptransaction", XrpTxs)
}