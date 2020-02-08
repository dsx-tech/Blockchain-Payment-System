package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object XrpTxTable: IntIdTable() {
    val amount: Column<BigDecimal> = decimal("amount", 30, 15)
    val fee: Column<BigDecimal> = decimal("fee", 30, 15)
    val hash: Column<String> = varchar("hash", 500)
    val account: Column<String> = varchar("account", 50)// адресс отправителя
    val destination: Column<String?> = varchar("destination", 500).nullable() // may be null адресс получателя
    val sequence: Column<Int> = integer("sequence")
    val validated: Column<Boolean?> = bool("validated").nullable()
    val TxId = reference("TxId", TxTable)
}