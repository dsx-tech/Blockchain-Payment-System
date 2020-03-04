package dsx.bps.DBservices

import com.sun.xml.internal.txw2.output.TXWSerializer
import dsx.bps.DBclasses.TxTable
import dsx.bps.DBclasses.TxEntity
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.TxStatus
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class TxService(datasource: Datasource) {
    init {
        datasource.getConnection()
        transaction {
            if (!TxTable.exists())
                SchemaUtils.create(TxTable)
        }
    }

    fun add(_status: TxStatus, _destination: String,
            _tag: Int?, _amount: BigDecimal,
            _fee: BigDecimal, _hash: String,
            _index: Int, _currency: Currency): TxEntity {
        val newTx = transaction{
            TxEntity.new {
                status = _status
                destination = _destination
                tag = _tag
                amount = _amount
                fee = _fee
                hash = _hash
                index = _index
                currency = _currency
            }
        }
        return newTx
    }

    fun getById(id: Int): TxEntity? {
        return transaction { TxEntity.findById(id)}
    }

    fun getByTxId(hash: String, index: Int): TxEntity {
        return transaction {TxEntity.find {TxTable.hash eq hash and (TxTable.index eq index)}.first()}
    }

    fun updateStatus(_status: TxStatus, hash: String, index: Int) {
        transaction { getByTxId(hash, index).status = _status }
    }
}