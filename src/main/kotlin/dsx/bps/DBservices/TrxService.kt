package dsx.bps.DBservices

import dsx.bps.DBclasses.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class TrxService() {
    init {
        Datasource.getConnection()
        transaction {
            if (!TrxTxTable.exists())
                SchemaUtils.create(TrxTxTable)
        }
    }

    fun add(_address: String, _contractRet: List<String>,
            _tx: TxEntity): TrxTxEntity {
        val newTrxTxEntity = transaction{
            TrxTxEntity.new {
                address = _address
                Tx = _tx
                _contractRet.forEach { addContractRet(it, this) }
            }
        }
        return newTrxTxEntity
    }

    fun addContractRet(cont: String, trxEntity: TrxTxEntity) {
        transaction {
            ContractRetEntity.new {
                contractRet = cont
                trx = trxEntity
            }
        }
    }

    fun delete(trxTx: TrxTxEntity) {
        transaction {trxTx.delete()}
    }

//    fun getByHash(hash: String): TrxTxEntity {
//        Database.connect(Datasource().getHicari())
//        return transaction { TrxTxEntity.find { TrxTxTable.hash eq hash}.first()}
//    }
}