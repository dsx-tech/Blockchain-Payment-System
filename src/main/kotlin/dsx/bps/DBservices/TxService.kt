package dsx.bps.DBservices

import dsx.bps.DBclasses.TxEntity
import dsx.bps.DBclasses.TxTable
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.TxStatus
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class TxService(datasource: Datasource) {
    init {
        transaction(datasource.getConnection()) {
            if (!TxTable.exists())
                SchemaUtils.create(TxTable)
        }
    }

    fun add(
        status: TxStatus, destination: String,
        tag: String?, amount: BigDecimal,
        fee: BigDecimal, hash: String,
        index: Long, currency: Currency
    ): TxEntity {
        val newTx = transaction {
            TxEntity.new {
                this.status = status
                this.destination = destination
                this.tag = tag
                this.amount = amount
                this.fee = fee
                this.hash = hash
                this.index = index
                this.currency = currency
            }
        }
        return newTx
    }

    fun getById(id: Int): TxEntity? {
        return transaction { TxEntity.findById(id) }
    }

    fun getByTxId(hash: String, index: Long): TxEntity {
        return transaction { TxEntity.find { TxTable.hash eq hash and (TxTable.index eq index) }.first() }
    }

    fun updateStatus(status: TxStatus, hash: String, index: Long) {
        return transaction { getByTxId(hash, index).status = status }
    }
}