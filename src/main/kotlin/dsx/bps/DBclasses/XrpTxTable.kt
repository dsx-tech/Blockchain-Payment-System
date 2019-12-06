package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object XrpTxTable: IntIdTable() {
    val amount: Column<String> = varchar("amoout", 100)
    val fee: Column<String> = varchar("fee", 100)
    val hash: Column<String> = varchar("hash", 500)
    val account: Column<String> = varchar("account", 50)// адресс отправителя
    val destination: Column<String?> = varchar("destination", 500).nullable() // may be null адресс получателя
    val sequence: Column<Int> = integer("sequence")
    val validated: Column<Boolean?> = bool("validated").nullable()
    val TxId = reference("TxId", TxTable)
}