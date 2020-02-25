package dsx.bps.DBservices

import dsx.bps.DBclasses.BtcTxEntity
import dsx.bps.DBclasses.BtcTxTable
import dsx.bps.DBclasses.TxEntity
import dsx.bps.core.datamodel.TxId
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class BtcService() {
    init {
        Datasource.getConnection()
        transaction {
            if (!BtcTxTable.exists())
                SchemaUtils.create(BtcTxTable)
        }
    }

    fun add( _confirmations: Int,
             _blockHash: String, _address: String,
             _tx: TxEntity): BtcTxEntity {
        val newBtcTxEntity = transaction{
            BtcTxEntity.new {
                confirmations = _confirmations
                blockHash = _blockHash
                address = _address
                Tx = _tx
            }
        }
        return newBtcTxEntity
    }

    fun delete(btcTx: BtcTxEntity) {
        transaction {btcTx.delete()}
    }

    fun getByBlockHash(blockHash: String): BtcTxEntity {
        return transaction {BtcTxEntity.find {BtcTxTable.blockHash eq blockHash}.first()}
    }
}