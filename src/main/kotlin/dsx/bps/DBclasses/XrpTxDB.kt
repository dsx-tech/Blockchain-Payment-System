package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object XrpTxs: IntIdTable() {
    val amount: Column<Int> = integer("amoout")
    val fee: Column<Int> = integer("fee")
    val hash: Column<String> = varchar("hash", 500)
    //val hex: Column<String> = varchar("hex", 500)
    val account: Column<String> = varchar("account", 50)// адресс отправителя
    val destination: Column<String> = varchar("destination", 500) // may be null адресс получателя
    val sequence: Column<Int> = integer("sequence")
    //val type: Column<String> = varchar("type", 500) у нас всегда payment
    // validated (bool) нужна
    val invoiceIndex: Column<Int> = integer("invoiceIndex")
    val invoice = reference("invoice", Invoices).nullable()
    val payment = reference("payment", Payments).nullable()
}