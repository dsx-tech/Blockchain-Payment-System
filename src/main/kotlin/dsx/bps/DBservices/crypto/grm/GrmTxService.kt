package dsx.bps.DBservices.crypto.grm

import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.core.tx.TxTable
import dsx.bps.DBclasses.crypto.grm.GrmInMsgEntity
import dsx.bps.DBclasses.crypto.grm.GrmInMsgTable
import dsx.bps.DBclasses.crypto.grm.GrmTxEntity
import dsx.bps.DBclasses.crypto.grm.GrmTxTable
import dsx.bps.DBservices.Datasource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class GrmTxService(datasource: Datasource) {

    init {
        transaction(datasource.getConnection()) {
            if (!GrmTxTable.exists())
                SchemaUtils.create(GrmTxTable)
        }
    }

    fun tableIsEmpty(): Boolean {
        return transaction { GrmTxTable.selectAll().empty() }
    }

    fun getGrmNewestKnownTx(): TxEntity {
        val grmNewestKnownTx = transaction {
            GrmTxEntity.find {
                GrmTxTable.txId eq TxTable.id and (TxTable.index eq TxTable.index.max())
            }.single()
        }

        return grmNewestKnownTx.tx
    }

    fun add(utime: Long, inInMsg: GrmInMsgEntity, tx: TxEntity): GrmTxEntity {
        return transaction {
            GrmTxEntity.new {
                this.utime = utime
                this.inMsg = inInMsg
                this.tx = tx
            }
        }
    }

    fun findByInMsgHashAndDest(inMsgHash: String, destination: String): GrmTxEntity? {
        return transaction {
            GrmTxEntity.find {
                ((GrmTxTable.inMsg eq GrmInMsgTable.id) and
                        (GrmInMsgTable.bodyHash eq inMsgHash) and
                        (GrmTxTable.txId eq TxTable.id) and
                        (TxTable.destination eq destination))
            }.singleOrNull()
        }
    }

    fun delete(grmTx: GrmTxEntity) {
        transaction { grmTx.delete() }
    }
}