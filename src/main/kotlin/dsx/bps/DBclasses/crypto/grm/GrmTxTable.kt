package dsx.bps.DBclasses.crypto.grm

import dsx.bps.DBclasses.core.TxTable
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object GrmTxTable : IntIdTable() {
    val utime: Column<Long> = long("utime")
    val lt: Column<Long> = long("lt")
    val txId = reference("TxId", TxTable)
}
