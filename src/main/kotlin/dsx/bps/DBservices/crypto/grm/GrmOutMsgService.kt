package dsx.bps.DBservices.crypto.grm

import dsx.bps.DBclasses.crypto.grm.GrmOutMsgEntity
import dsx.bps.DBclasses.crypto.grm.GrmOutMsgTable
import dsx.bps.DBclasses.crypto.grm.GrmTxEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

class GrmOutMsgService {

    fun add(source: String, destination: String,
            value: Long, fwdFee: Long,
            ihrFee: Long, createdLt: Long,
            bodyHash: String, msgText: String,
            grmTx: GrmTxEntity
    ): GrmOutMsgEntity {
        return transaction {
            GrmOutMsgEntity.new {
                this.sourceAddress = source
                this.destination = destination
                this.value = value
                this.fwdFee = fwdFee
                this.ihrFee = ihrFee
                this.createdLt = createdLt
                this.bodyHash = bodyHash
                this.msgText = msgText
                this.grmTx = grmTx
            }
        }
    }

    fun getOutMsgs(grmTxId: EntityID<Int>): List<GrmOutMsgEntity> {
        return transaction {
            GrmOutMsgEntity.find {
                GrmOutMsgTable.id eq grmTxId
            }
        }.toList()
    }

}