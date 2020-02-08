package dsx.bps.DBservices

import dsx.bps.DBclasses.XrpTxEntity
import dsx.bps.DBclasses.XrpTxTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class XrpService {
    fun create() {
        Database.connect(Datasource().getHicari())
        transaction { SchemaUtils.create(XrpTxTable) }
    }

    fun add(_amount: BigDecimal, _fee: BigDecimal,
            _hash: String, _account: String,
            _destination: String?, _sequence: Int,
            _validated: Boolean?): XrpTxEntity {
        Database.connect(Datasource().getHicari())
        val newXrpTxEntity = transaction{
            XrpTxEntity.new {
                amount = _amount
                fee = _fee
                hash = _hash
                account = _account
                destination = _destination
                sequence = _sequence
                validated = _validated
            }
        }
        return newXrpTxEntity
    }

    fun delete(xrpTx: XrpTxEntity) {
        Database.connect(Datasource().getHicari())
        transaction {xrpTx.delete()}
    }

    fun getByHash(hash: String): XrpTxEntity {
        Database.connect(Datasource().getHicari())
        return transaction { XrpTxEntity.find { XrpTxTable.hash eq hash}.first()}
    }
}