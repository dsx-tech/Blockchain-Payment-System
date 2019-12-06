package dsx.bps.DBservices

import dsx.bps.DBclasses.BtcTxEntity
import dsx.bps.DBclasses.BtcTxTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class BtcService {
    fun create() {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction { SchemaUtils.create(BtcTxTable) }
    }

    fun add(_amount: String, _fee: String?,
            _confirmations: Int, _blockHash: String,
            _hash: String, _adress: String): BtcTxEntity {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        val newBtcTxEntity = transaction{
            BtcTxEntity.new {
                amount = _amount
                fee = _fee
                confirmations = _confirmations
                blockHash = _blockHash
                hash = _hash
                address = _adress
            }
        }
        return newBtcTxEntity
    }

    fun delete(btcTx: BtcTxEntity) {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver", //pull conectoв
            user = "root", password = "root")
        transaction {btcTx.delete()}
    }

    fun getByHash(hash: String): BtcTxEntity {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        return transaction {BtcTxEntity.find {BtcTxTable.hash eq hash}.first()}
    }
}