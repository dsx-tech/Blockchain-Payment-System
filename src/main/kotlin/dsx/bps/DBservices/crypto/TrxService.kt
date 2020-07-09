package dsx.bps.DBservices.crypto

import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.crypto.trx.ContractRetEntity
import dsx.bps.DBclasses.crypto.trx.TrxTxEntity
import org.jetbrains.exposed.sql.transactions.transaction

class TrxService {

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