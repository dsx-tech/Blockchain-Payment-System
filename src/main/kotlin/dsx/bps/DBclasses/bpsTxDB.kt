package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object BtcTxs: IntIdTable() {
    val amount: Column<Int> = integer("amoout")
    val fee: Column<Int> = integer("fee")
    val confirmations: Column<Int> = integer("confirmations")
    val blockHash: Column<String> = varchar("blockHash", 500)
    val hash: Column<String> = varchar("hash", 500)
    //val hex: Column<String> = varchar("hex", 500)
    val adress: Column<String> = varchar("adress", 100)
    val invoiceIndex: Column<Int> = integer("invoiceIndex")
    val invoice = reference("invoice", Invoices).nullable()
    val payment = reference("payment", Payments).nullable()
}
