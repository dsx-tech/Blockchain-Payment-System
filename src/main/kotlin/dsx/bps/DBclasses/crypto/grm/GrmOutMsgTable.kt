package dsx.bps.DBclasses.crypto.grm

import dsx.bps.DBclasses.crypto.grm.GrmConstant.outMsgTextMaxLength
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object GrmOutMsgTable : IntIdTable() {
    // sourceAddress == GrmRawMessage.source
    val sourceAddress: Column<String> = GrmInMsgTable.varchar("sourceAddress", 1024)
    val destination: Column<String> = GrmInMsgTable.varchar("destination", 1024)
    val value: Column<Long> = GrmInMsgTable.long("value")
    val fwdFee: Column<Long> = GrmInMsgTable.long("fwdFee")
    val ihrFee: Column<Long> = GrmInMsgTable.long("ihrFee")
    val createdLt: Column<Long> = GrmInMsgTable.long("createdLt")
    val bodyHash: Column<String> = GrmInMsgTable.varchar("bodyHash", 1024)
    val msgText: Column<String> = GrmInMsgTable.varchar("msgText", outMsgTextMaxLength)
    val grmTx = reference("grmTx", GrmTxTable)
}