package dsx.bps.DBservices

import dsx.bps.DBclasses.TrxTxEntity
import dsx.bps.DBclasses.TrxTxTable
import dsx.bps.DBclasses.TxEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class TrxService(connectionURL: String, driver: String) {
    init {
        Datasource.getHicari(connectionURL, driver)
        transaction {
            if (!TrxTxTable.exists())
                SchemaUtils.create(TrxTxTable)
        }
    }

    fun add(//_address: String, _contractRet: String,
            _tx: TxEntity): TrxTxEntity {
        val newTrxTxEntity = transaction{
            TrxTxEntity.new {
                //address = _address
                //contractRet = _contractRet
                Tx = _tx
            }
        }
        return newTrxTxEntity
    }

    fun delete(trxTx: TrxTxEntity) {
        transaction {trxTx.delete()}
    }

//    fun getByHash(hash: String): TrxTxEntity {
//        Database.connect(Datasource().getHicari())
//        return transaction { TrxTxEntity.find { TrxTxTable.hash eq hash}.first()}
//    }
}