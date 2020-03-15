package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object GrmTxTable : IntIdTable() {
    //TODO: Что еще нужно хранить в бд?
    val utime: Column<Long> = long("utime")
    val storageFee: Column<Long> = long("storageFee")
    val lt: Column<Long> = long("lt")
    val txId = reference("TxId", TxTable)
}
