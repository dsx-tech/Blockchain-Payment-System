package dsx.bps.DBservices.crypto.grm

import dsx.bps.DBclasses.core.CryptoAddressEntity
import dsx.bps.DBclasses.core.CryptoAddressTable
import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.crypto.grm.GrmInMsgEntity
import dsx.bps.DBclasses.crypto.grm.GrmInMsgTable
import dsx.bps.DBclasses.crypto.grm.GrmTxEntity
import dsx.bps.DBclasses.crypto.grm.GrmTxTable
import dsx.bps.core.datamodel.Currency
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class GrmTxService {

    fun tableIsEmpty(): Boolean {
        return transaction { GrmTxTable.selectAll().empty() }
    }

    fun getGrmNewestKnownTx(): GrmTxEntity? {
        val maxIndexTxEntity = transaction {
            val grmTxs = mutableListOf<TxEntity>()
            CryptoAddressEntity.find {
                CryptoAddressTable.currency eq Currency.GRM
            }.map { it.txs.toList() }.forEach { grmTxs.addAll(it) }

            grmTxs.maxBy { tx -> tx.index}
        }
        return if (maxIndexTxEntity == null)
            null
        else {
            transaction {
                GrmTxEntity.find {
                    GrmTxTable.txId eq maxIndexTxEntity.id
                }.singleOrNull()
            }
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
        val inMsgEntity = transaction {
            GrmInMsgEntity.find {
                GrmInMsgTable.bodyHash eq inMsgHash
            }.singleOrNull()
        }

        return if (inMsgEntity == null) {
            null
        } else {
            transaction {
                GrmTxEntity.find {
                    (GrmTxTable.inMsg eq inMsgEntity.id)
                }.singleOrNull()
            }
        }
    }

    fun delete(grmTx: GrmTxEntity) {
        transaction { grmTx.delete() }
    }
}