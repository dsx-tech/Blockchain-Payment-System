package dsx.bps.DBservices.grm

import dsx.bps.DBclasses.grm.GrmLastTxPayDestAccEntity
import dsx.bps.DBclasses.grm.GrmLastTxPayDestAccTable
import dsx.bps.DBservices.Datasource
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

class GrmLastTxPayDestAccService(datasource: Datasource) {

    init {
        transaction(datasource.getConnection()) {
            if (!GrmLastTxPayDestAccTable.exists())
                SchemaUtils.create(GrmLastTxPayDestAccTable)
        }
    }

    fun add(
        destAccountAddress: String, paymentTxLt: Long, paymentTxHash: String,
        lastProcTxLt: Long, lastProcTxHash: String
    ): GrmLastTxPayDestAccEntity {
        return transaction {
            GrmLastTxPayDestAccEntity.new {
                this.destAccountAddress = destAccountAddress
                this.paymentTxLt = paymentTxLt
                this.paymentTxHash = paymentTxHash
                this.lastProcTxLt = lastProcTxLt
                this.lastProcTxHash = lastProcTxHash
            }
        }
    }

    fun get(
        destAccountAddress: String, paymentTxLt: Long,
        paymentTxHash: String
    ): GrmLastTxPayDestAccEntity {
        return transaction {
            GrmLastTxPayDestAccEntity.find {
                (GrmLastTxPayDestAccTable.destAccountAddress eq destAccountAddress) and
                        (GrmLastTxPayDestAccTable.paymentTxLt eq paymentTxLt) and
                        (GrmLastTxPayDestAccTable.paymentTxHash eq paymentTxHash)
            }.single()
        }
    }

    fun updateLastTx(entity: GrmLastTxPayDestAccEntity, newLt: Long, newHash: String) {
        transaction {
            entity.lastProcTxLt = newLt
            entity.lastProcTxHash = newHash
        }
    }

    fun delete(grmLastTxPayDestAccEntity: GrmLastTxPayDestAccEntity) {
        transaction { grmLastTxPayDestAccEntity.delete() }
    }
}