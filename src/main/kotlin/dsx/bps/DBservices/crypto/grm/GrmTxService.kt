package dsx.bps.DBservices.crypto.grm

import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.core.tx.TxTable
import dsx.bps.DBclasses.crypto.grm.GrmInMsgEntity
import dsx.bps.DBclasses.crypto.grm.GrmInMsgTable
import dsx.bps.DBclasses.crypto.grm.GrmTxEntity
import dsx.bps.DBclasses.crypto.grm.GrmTxTable
import dsx.bps.DBservices.Datasource
import dsx.bps.core.datamodel.Currency
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

    fun getGrmNewestKnownTx(): GrmTxEntity? {
        return transaction {
            GrmTxEntity.find {
                GrmTxTable.txId eq TxTable.id and
                        (TxTable.index eq TxTable.index.max()) and
                        (TxTable.currency eq Currency.GRM)
            }.singleOrNull()
        }
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

    fun findByInMsgHash(inMsgHash: String): GrmTxEntity? {
        return transaction {
            GrmTxEntity.find {
                ((GrmTxTable.inMsg eq GrmInMsgTable.id) and
                        (GrmInMsgTable.bodyHash eq inMsgHash))
            }.singleOrNull()
        }
    }

    fun delete(grmTx: GrmTxEntity) {
        transaction { grmTx.delete() }
    }
}