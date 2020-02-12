package dsx.bps.DBservices

import dsx.bps.DBclasses.BtcTxEntity
import dsx.bps.DBclasses.BtcTxTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class BtcService(connectionURL: String, driver: String) {
    init {
        Database.connect(Datasource().getHicari(connectionURL, driver))
        transaction {
            if (!BtcTxTable.exists())
                SchemaUtils.create(BtcTxTable)
        }
    }

    fun add(_amount: BigDecimal, _fee: BigDecimal?,
            _confirmations: Int, _blockHash: String,
            _adress: String): BtcTxEntity {
        val newBtcTxEntity = transaction{
            BtcTxEntity.new {
                amount = _amount
                fee = _fee
                confirmations = _confirmations
                blockHash = _blockHash
                address = _adress
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