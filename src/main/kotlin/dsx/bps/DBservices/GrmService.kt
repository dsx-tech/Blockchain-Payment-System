package dsx.bps.DBservices

import dsx.bps.DBclasses.GrmTxEntity
import dsx.bps.DBclasses.GrmTxTable
import dsx.bps.DBclasses.TxEntity
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class GrmService(datasource: Datasource) {

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
                GrmTxTable.lt eq GrmTxTable.lt.max()
            }.single()
        }

        return grmNewestKnownTx.tx
    }

    fun add(utime: Long, storageFee: Long, lt: Long, tx: TxEntity): GrmTxEntity {
        return transaction {
            GrmTxEntity.new {
                this.utime = utime
                this.storageFee = storageFee
                this.lt = lt
                this.tx = tx
            }
        }
    }

    fun delete(grmTx: GrmTxEntity) {
        transaction { grmTx.delete() }
    }
}