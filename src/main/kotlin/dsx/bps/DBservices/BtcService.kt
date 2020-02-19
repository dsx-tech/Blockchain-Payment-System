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

class BtcService(connectionURL: String, driver: String) {
    init {
        Datasource.getHicari(connectionURL, driver)
        transaction {
            if (!BtcTxTable.exists())
                SchemaUtils.create(BtcTxTable)
        }
    }

    fun add( _fee: BigDecimal?, _confirmations: Int,
             _blockHash: String, _address: String,
             _tx: TxEntity): BtcTxEntity {
        val newBtcTxEntity = transaction{
            BtcTxEntity.new {
                fee = _fee
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

//    fun getByHash(hash: String): BtcTxEntity {
//        Database.connect(Datasource().getHicari())
//        return transaction {BtcTxEntity.find {BtcTxTable.hash eq hash}.first()}
//    }
}