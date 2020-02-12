package dsx.bps.DBservices

import dsx.bps.DBclasses.TrxTxEntity
import dsx.bps.DBclasses.TrxTxTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class TrxService(connectionURL: String, driver: String) {
    init {
        Database.connect(Datasource().getHicari(connectionURL, driver))
        transaction {
            if (!TrxTxTable.exists())
                SchemaUtils.create(TrxTxTable)
        }
    }

    fun add(_amount: BigDecimal, _address: String,
            _contractRet: String): TrxTxEntity {
        val newTrxTxEntity = transaction{
            TrxTxEntity.new {
                amount = _amount
                address = _address
                contractRet = _contractRet
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