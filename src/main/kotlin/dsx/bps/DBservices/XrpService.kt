package dsx.bps.DBservices

import dsx.bps.DBclasses.Invoice
import dsx.bps.DBclasses.Payment
import dsx.bps.DBclasses.XrpTx
import dsx.bps.DBclasses.XrpTxs
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class XrpService {
    fun create() { // make  static
        SchemaUtils.create(XrpTxs)
    }

    fun add(_amount: Int, _fee: Int,
            _hash: String, _hex: String,
            _account: String, _destination: String,
            _sequence: Int, _type: String,
            _invoiceIndex: Int, _invoice: Invoice?,
            _payment: Payment?): XrpTx {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        val newXrpTx = transaction{
            XrpTx.new {
                amount = _amount
                fee = _fee
                hash = _hash
                //hex = _hex
                account = _account
                destination = _destination
                sequence = _sequence
                //type = _type
                invoiceIndex = _invoiceIndex
                invoice = _invoice
                payment = _payment
            }
        }
        return newXrpTx
    }

    fun delete(xrpTx: XrpTx) {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction {xrpTx.delete()}
    }

    fun getByHash(hash: String): XrpTx {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        return transaction { XrpTx.find { XrpTxs.hash eq hash}.first()}
    }
}