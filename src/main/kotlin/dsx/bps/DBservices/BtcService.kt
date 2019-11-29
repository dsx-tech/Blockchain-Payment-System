package dsx.bps.DBservices

import dsx.bps.DBclasses.BtcTx
import dsx.bps.DBclasses.BtcTxs
import dsx.bps.DBclasses.Invoice
import dsx.bps.DBclasses.Payment
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class BtcService {
    fun create() { // make  static
        SchemaUtils.create(BtcTxs)
    }

    fun add(_amount: Int, _fee: Int,
               _confirmations: Int, _blockHash: String,
               _hash: String, _hex: String,
               _adress: String, _invoiceIndex: Int,
               _invoice: Invoice?, _payment: Payment?): BtcTx {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        val newBtcTx = transaction{
            BtcTx.new {
                amount = _amount
                fee = _fee
                confirmations = _confirmations
                blockHash = _blockHash
                hash = _hash
                //hex = _hex
                adress = _adress
                invoiceIndex = _invoiceIndex
                invoice = _invoice
                payment = _payment
            }
        }
        return newBtcTx
    }

    fun delete(btcTx: BtcTx) {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction {btcTx.delete()}
    }

    fun getByHash(hash: String): BtcTx {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        return transaction {BtcTx.find {BtcTxs.hash eq hash}.first()}
    }
}