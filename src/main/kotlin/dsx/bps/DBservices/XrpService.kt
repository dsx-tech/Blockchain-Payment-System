package dsx.bps.DBservices

import dsx.bps.DBclasses.XrpTxEntity
import dsx.bps.DBclasses.XrpTxTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class XrpService {
    fun create() {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction { SchemaUtils.create(XrpTxTable) }
    }

    fun add(_amount: String, _fee: String,
            _hash: String, _account: String,
            _destination: String?, _sequence: Int,
            _validated: Boolean?): XrpTxEntity {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
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
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction {xrpTx.delete()}
    }

    fun getByHash(hash: String): XrpTxEntity {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        return transaction { XrpTxEntity.find { XrpTxTable.hash eq hash}.first()}
    }
}