package dsx.bps.DBservices

import dsx.bps.DBclasses.*
import dsx.bps.DBclasses.PayableEntity
import dsx.bps.core.datamodel.Payment
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class InvoiceService {
    fun create() {
        Database.connect(Datasource().getHicari())
        transaction { SchemaUtils.create(InvoiceTable) }
    }

    fun add(_status: String, _received: BigDecimal,
            _invoiceId: String, _currency: String,
            _amount: BigDecimal, _address: String,
            _tag: Int?): InvoiceEntity {
        Database.connect(Datasource().getHicari())
        val newInvoice = transaction{
            InvoiceEntity.new {
                status = _status
                received = _received
                invoiceId = _invoiceId
                currency = _currency
                amount = _amount
                address = _address
                tag = _tag
                payable = PayableEntity.new { type = "invoice" }
            }
        }
        return newInvoice
    }

    fun delete(invoice: InvoiceEntity) {
        Database.connect(Datasource().getHicari())
        transaction {invoice.delete()}
    }

    fun getById(id: Int): InvoiceEntity? {
        Database.connect(Datasource().getHicari())// url как поле класса в конструкторе и конфиге
        return transaction { InvoiceEntity.findById(id)}
    }

    fun getBySystemId(id: String): InvoiceEntity {
        Database.connect(Datasource().getHicari())// url как поле класса в конструкторе и конфиге
        return transaction {InvoiceEntity.find {InvoiceTable.invoiceId eq id}.first()}
    }

    fun updateStatus(_status: String, invoice: InvoiceEntity) {
        Database.connect(Datasource().getHicari())
        transaction { invoice.status = _status }
    }

    fun updateReceived(_received: BigDecimal, invoice: InvoiceEntity) {
        Database.connect(Datasource().getHicari())
        transaction { invoice.received = _received }

    }
}