package dsx.bps.DBservices

import dsx.bps.DBclasses.TrxTxEntity
import dsx.bps.DBclasses.TrxTxTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class TrxService {
    fun create() {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction { SchemaUtils.create(TrxTxTable) }
    }

    fun add(_amount: String, _hash: String,
            _adress: String, _contractRet: String): TrxTxEntity {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        val newTrxTxEntity = transaction{
            TrxTxEntity.new {
                amount = _amount
                hash = _hash
                address = _adress
                contractRet = _contractRet
            }
        }
        return newTrxTxEntity
    }

    fun delete(trxTx: TrxTxEntity) {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction {trxTx.delete()}
    }

    fun getByHash(hash: String): TrxTxEntity {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        return transaction { TrxTxEntity.find { TrxTxTable.hash eq hash}.first()}
    }
}