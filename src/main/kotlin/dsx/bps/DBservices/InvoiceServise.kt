package dsx.bps.DBservices

import dsx.bps.DBclasses.Invoice
import dsx.bps.DBclasses.Invoices
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class InvoiceService {
    fun create() { // make  static?
        SchemaUtils.create(Invoices)
    }

    fun add(_status: String, _received: Int): Invoice {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        val newInvoice = transaction{
            Invoice.new {
                status = _status
                received = _received
            }
        }
        return newInvoice
    }

    fun delete(invoice: Invoice) {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction {invoice.delete()}
    }

    fun getById(id: Int): Invoice? {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")// url как поле класса в конструкторе и конфиге
        return transaction { Invoice.findById(id)}
    }

    fun updateStatus(_status: String, invoice: Invoice) {
        invoice.status = _status
    }

    fun updateReceived(_received: Int, invoice: Invoice) {
        invoice.received = _received
    }
}