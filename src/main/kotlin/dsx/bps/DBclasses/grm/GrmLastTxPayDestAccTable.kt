package dsx.bps.DBclasses.grm

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object GrmLastTxPayDestAccTable : IntIdTable() {
    val destAccountAddress: Column<String> = varchar("destAccountAddress", 48)
    val paymentTxLt: Column<Long> = long("paymentTxLt")
    val paymentTxHash: Column<String> = varchar("paymentTxHash", 64)
    val lastProcTxLt: Column<Long> = long("lastProcTxLt")
    val lastProcTxHash: Column<String> = varchar("lastProcTxHash", 64)
}