package dsx.bps.DBservices

import dsx.bps.DBclasses.TxTable
import dsx.bps.DBclasses.TxEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class TxService {
    init {
        Database.connect(Datasource().getHicari())
        transaction {
            if (!TxTable.exists())
                SchemaUtils.create(TxTable)
        }
    }

    fun add(_status: String, _destination: String,
            _tag: Int?, _fee: BigDecimal,
            _hash: String, _index: Int,
            _currency: String): TxEntity {
        Database.connect(Datasource().getHicari())
        val newTx = transaction{
            TxEntity.new {
                status = _status
                destination = _destination
                tag = _tag
                fee = _fee
                hash = _hash
                index = _index
                currency = _currency
            }
        }
        return newTx
    }

    fun getById(id: Int): TxEntity? {
        Database.connect(Datasource().getHicari())
        return transaction { TxEntity.findById(id)}
    }

    fun getByTxId(hash: String, index: Int): TxEntity {
        Database.connect(Datasource().getHicari())// url как поле класса в конструкторе и конфиге
        return transaction {TxEntity.find {TxTable.hash eq hash and (TxTable.index eq index)}.first()}
    }
}