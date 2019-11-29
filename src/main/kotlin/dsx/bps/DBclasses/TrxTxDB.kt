package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object TrxTxs: IntIdTable() {
    val amount: Column<Int> = integer("amoout")
    val hash: Column<String> = varchar("hash", 500)
    val hex: Column<String> = varchar("hex", 500)
    //val signature: Column<List<String>> = varchar("signature", 500) // may be null, list?
    val adress: Column<String> = varchar("adress", 100)
    val contractRet: Column<String> = varchar("contractRet", 100)
    val invoiceIndex: Column<Int> = integer("invoiceIndex")
    val invoice = reference("invoice", Invoices).nullable()
    val payment = reference("payment", Payments).nullable()
}