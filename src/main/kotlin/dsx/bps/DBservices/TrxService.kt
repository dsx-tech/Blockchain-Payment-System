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

    fun add(
        address: String, contractRet: List<String>,
        tx: TxEntity
    ): TrxTxEntity {
        val newTrxTxEntity = transaction {
            TrxTxEntity.new {
                this.address = address
                this.tx = tx
                contractRet.forEach { addContractRet(it, this) }
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
        transaction { trxTx.delete() }
    }
}