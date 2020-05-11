package dsx.bps.DBclasses.crypto.eth

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object EthAccountTable: IntIdTable() {
    val address: Column<String> = varchar("address", 100)
    val password: Column<String> = varchar("password", 100)
    val wallet: Column<String> = varchar("wallet", 100)
    var contractAddress: Column<String> = varchar("contractAddress", 100)
}