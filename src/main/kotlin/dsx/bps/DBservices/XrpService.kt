package dsx.bps.DBservices

import dsx.bps.DBclasses.TxEntity
import dsx.bps.DBclasses.XrpTxEntity
import dsx.bps.DBclasses.XrpTxTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class XrpService(connectionURL: String, driver: String) {
    init {
        Datasource.getHicari(connectionURL, driver)
        transaction {
            if (!XrpTxTable.exists())
                SchemaUtils.create(XrpTxTable)
        }
    }

    fun add(_fee: BigDecimal, _account: String,
            _destination: String?, _sequence: Int,
            _validated: Boolean?, _tx: TxEntity): XrpTxEntity {
        val newXrpTxEntity = transaction{
            XrpTxEntity.new {
                fee = _fee
                account = _account
                destination = _destination
                sequence = _sequence
                validated = _validated
                Tx = _tx
            }
        }
        return newXrpTxEntity
    }

    fun delete(xrpTx: XrpTxEntity) {
        transaction {xrpTx.delete()}
    }

//    fun getByHash(hash: String): XrpTxEntity {
//        Database.connect(Datasource().getHicari())
//        return transaction { XrpTxEntity.find { XrpTxTable.hash eq hash}.first()}
//    }
}