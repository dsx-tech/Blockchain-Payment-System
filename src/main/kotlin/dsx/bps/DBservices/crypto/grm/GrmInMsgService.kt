package dsx.bps.DBservices.crypto.grm

import dsx.bps.DBclasses.crypto.grm.GrmInMsgEntity
import dsx.bps.DBclasses.crypto.grm.GrmInMsgTable
import dsx.bps.DBservices.Datasource
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

class GrmInMsgService(datasource: Datasource) {

    init {
        transaction(datasource.getConnection()) {
            if (!GrmInMsgTable.exists())
                SchemaUtils.create(GrmInMsgTable)
        }
    }

    fun add(source: String, destination: String,
            value: Long, fwdFee: Long,
            ihrFee: Long, createdLt: Long,
            bodyHash: String, msgText: String
    ): GrmInMsgEntity {
        return transaction {
            GrmInMsgEntity.new {
                this.sourceAddress = source
                this.destination = destination
                this.value = value
                this.fwdFee = fwdFee
                this.ihrFee = ihrFee
                this.createdLt = createdLt
                this.bodyHash = bodyHash
                this.msgText = msgText
            }
        }
    }
}