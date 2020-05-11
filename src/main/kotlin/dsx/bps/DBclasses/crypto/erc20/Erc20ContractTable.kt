package dsx.bps.DBclasses.crypto.erc20

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object Erc20ContractTable: IntIdTable() {
    var contractAddress: Column<String> = varchar("contractAddress", 100)
    val decimals = integer("decimals")
    val owner = varchar("owner", 100)
}