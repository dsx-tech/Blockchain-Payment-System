package dsx.bps.DBclasses.crypto.grm

import dsx.bps.DBclasses.crypto.grm.GrmConstant.inMsgTextMaxLength
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object GrmInMsgTable : IntIdTable() {
    // sourceAddress == GrmRawMessage.source
    val sourceAddress: Column<String> = varchar("sourceAddress", 1024)
    val destination: Column<String> = varchar("destination", 1024)
    val value: Column<Long> = long("value")
    val fwdFee: Column<Long> = long("fwdFee")
    val ihrFee: Column<Long> = long("ihrFee")
    val createdLt: Column<Long> = long("createdLt")
    val bodyHash: Column<String> = varchar("bodyHash", 1024)
    val msgText: Column<String> = varchar("msgText", inMsgTextMaxLength)
}