package dsx.bps.DBservices

import dsx.bps.DBclasses.InvoiceEntity
import dsx.bps.DBclasses.InvoiceTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class InvoiceService {
    fun create() {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction { SchemaUtils.create(InvoiceTable) }
    }

    fun add(_status: String, _received: String,
            _invoiceId: String, _currency: String,
            _amount: String, _address: String,
            _tag: Int?): InvoiceEntity {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        val newInvoice = transaction{
            InvoiceEntity.new {
                status = _status
                received = _received
                invoiceId = _invoiceId
                currency = _currency
                amount = _amount
                address = _address
                tag = _tag
            }
        }
        return newInvoice
    }

    fun delete(invoice: InvoiceEntity) {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction {invoice.delete()}
    }

    fun getById(id: Int): InvoiceEntity? {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")// url как поле класса в конструкторе и конфиге
        return transaction { InvoiceEntity.findById(id)}
    }

    fun getBySystemId(id: String): InvoiceEntity {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")// url как поле класса в конструкторе и конфиге
        return transaction {InvoiceEntity.find {InvoiceTable.invoiceId eq id}.first()}
    }

    fun updateStatus(_status: String, invoice: InvoiceEntity) {
        invoice.status = _status
    }

    fun updateReceived(_received: String, invoice: InvoiceEntity) {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction { invoice.received = _received }

    }
}