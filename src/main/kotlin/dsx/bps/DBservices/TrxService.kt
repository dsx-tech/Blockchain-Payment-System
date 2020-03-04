package dsx.bps.DBservices

import dsx.bps.DBclasses.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

class TrxService(datasource: Datasource) {
    init {
        datasource.getConnection()
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
}