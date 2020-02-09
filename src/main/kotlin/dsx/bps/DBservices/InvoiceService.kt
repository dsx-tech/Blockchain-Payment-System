package dsx.bps.DBservices

import dsx.bps.DBclasses.*
import dsx.bps.DBclasses.PayableEntity
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Invoice
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.TxId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

class InvoiceService {
    init {
        Database.connect(Datasource().getHicari())
        transaction {
            if (!InvoiceTable.exists())
                SchemaUtils.create(InvoiceTable)
            if (!TxTable.exists())
                SchemaUtils.create(TxTable)
        }
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

    fun getAll(): SizedIterable<InvoiceEntity> {
        return InvoiceEntity.all()
    }

    fun getUnpaid(): ConcurrentHashMap.KeySetView<String, Boolean> {
        Database.connect(Datasource().getHicari())
        val unpaidInv = transaction { InvoiceEntity.find {InvoiceTable.status eq "unpaid"} }
        val unpaid = ConcurrentHashMap.newKeySet<String>()
        transaction {
            unpaidInv.forEach { unpaid.add(it.invoiceId) }
        }
        return unpaid
    }

    fun getInvoices(): ConcurrentHashMap<String, Invoice> {
        val invoiceMap = ConcurrentHashMap<String, Invoice>()
        Database.connect(Datasource().getHicari())
        transaction {
            InvoiceEntity.all().forEach {
                invoiceMap[it.invoiceId] = makeInvFromDB(it)
            }
        }
        return invoiceMap
    }

    fun addTx(inv: Invoice,tx: TxId) {
        Database.connect(Datasource().getHicari())
        transaction {
            TxEntity.find {TxTable.hash eq tx.hash and (TxTable.index eq tx.index)}.forEach {
                it.payable = getBySystemId(inv.id).payable
            }
        }
    }

    fun makeInvFromDB (invoice: InvoiceEntity): Invoice {
        val currency: Currency
        currency = when (invoice.currency){
            "BTC" -> Currency.BTC
            "XRP" -> Currency.XRP
            else -> Currency.TRX // else is necessarily, throw exception?(which never happen if all right)
        }
        val inv = Invoice(invoice.invoiceId, currency, invoice.amount, invoice.address, invoice.tag)
        inv.received = invoice.received
        invoice.payable.txs.forEach { inv.txids.add(TxId(it.hash, it.index)) }
        return inv
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