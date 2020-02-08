package dsx.bps.DBservices

import dsx.bps.DBclasses.TrxTxEntity
import dsx.bps.DBclasses.TrxTxTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class TrxService {
    fun create() {
        Database.connect(Datasource().getHicari())
        transaction { SchemaUtils.create(TrxTxTable) }
    }

    fun add(_amount: BigDecimal, _hash: String,
            _address: String, _contractRet: String): TrxTxEntity {
        Database.connect(Datasource().getHicari())
        val newTrxTxEntity = transaction{
            TrxTxEntity.new {
                amount = _amount
                hash = _hash
                address = _address
                contractRet = _contractRet
            }
        }
        return newTrxTxEntity
    }

    fun delete(trxTx: TrxTxEntity) {
        Database.connect(Datasource().getHicari())
        transaction {trxTx.delete()}
    }

    fun getByHash(hash: String): TrxTxEntity {
        Database.connect(Datasource().getHicari())
        return transaction { TrxTxEntity.find { TrxTxTable.hash eq hash}.first()}
    }
}