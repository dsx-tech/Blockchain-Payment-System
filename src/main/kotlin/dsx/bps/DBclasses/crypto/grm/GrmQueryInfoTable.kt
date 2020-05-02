package dsx.bps.DBclasses.crypto.grm

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object GrmQueryInfoTable : IntIdTable() {
    val queryId: Column<Long> = long("queryId")
    val validUntil: Column<Long> = long("validUntil")
    val bodyHash: Column<String> = varchar("bodyHash", 1024)
}