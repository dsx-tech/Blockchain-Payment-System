package dsx.bps.DBservices

import dsx.bps.DBclasses.Invoice
import dsx.bps.DBclasses.Payment
import dsx.bps.DBclasses.TrxTx
import dsx.bps.DBclasses.TrxTxs
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class TrxService {
    fun create() { // make  static
        SchemaUtils.create(TrxTxs)
    }

    fun add(_amount: Int, _hash: String,
            _hex: String, _adress: String,
            _contractRet: String, _invoiceIndex: Int,
            _invoice: Invoice?, _payment: Payment?): TrxTx {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        val newTrxTx = transaction{
            TrxTx.new {
                amount = _amount
                hash = _hash
                hex = _hex
                adress = _adress
                contractRet = _contractRet
                invoiceIndex = _invoiceIndex
                invoice = _invoice
                payment = _payment
            }
        }
        return newTrxTx
    }

    fun delete(trxTx: TrxTx) {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction {trxTx.delete()}
    }

    fun getByHash(hash: String): TrxTx {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        return transaction { TrxTx.find { TrxTxs.hash eq hash}.first()}
    }
}