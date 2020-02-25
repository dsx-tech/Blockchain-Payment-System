package dsx.bps.DBservices

import dsx.bps.DBclasses.*
import dsx.bps.DBclasses.PayableEntity
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Invoice
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.TxId
import dsx.bps.exception.DBservices.BpsDatabaseException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

class InvoiceService() {
    init {
        Datasource.getConnection()
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

    fun delete(systemId: String) {
        transaction {getBySystemId(systemId).delete()}
    }

    fun getById(id: Int): InvoiceEntity? {
        return transaction { InvoiceEntity.findById(id)}
    }

    fun getBySystemId(id: String): InvoiceEntity {
        return transaction {InvoiceEntity.find {InvoiceTable.invoiceId eq id}.first()}
    }

    fun getAll(): SizedIterable<InvoiceEntity> {
        return InvoiceEntity.all()
    }

    fun getUnpaid(): ConcurrentHashMap.KeySetView<String, Boolean> {
        val unpaidInv = transaction { InvoiceEntity.find {InvoiceTable.status eq "unpaid"} }
        val unpaid = ConcurrentHashMap.newKeySet<String>()
        transaction {
            unpaidInv.forEach { unpaid.add(it.invoiceId) }
        }
        return unpaid
    }

    fun getInvoices(): ConcurrentHashMap<String, Invoice> {
        val invoiceMap = ConcurrentHashMap<String, Invoice>()
        transaction {
            InvoiceEntity.all().forEach {
                invoiceMap[it.invoiceId] = makeInvFromDB(it)
            }
        }
        return invoiceMap

    }

    fun addTx(systemId: String,tx: TxId) {
        transaction {
            TxEntity.find {TxTable.hash eq tx.hash and (TxTable.index eq tx.index)}.forEach {
                it.payable = getBySystemId(systemId).payable
            }
        }
    }

    fun makeInvFromDB (invoice: InvoiceEntity): Invoice {
        val currency: Currency
        currency = when (invoice.currency){
            "btc" -> Currency.BTC
            "BTC" -> Currency.BTC
            "xrp" -> Currency.XRP
            "XRP" -> Currency.XRP
            "trx" -> Currency.TRX
            "TRX" -> Currency.TRX
            else -> throw BpsDatabaseException("wrong currency")
        }
        val inv = Invoice(invoice.invoiceId, currency, invoice.amount.stripTrailingZeros().add(BigDecimal.ZERO),
                          invoice.address, invoice.tag)//amount zeroes problem better options?
        inv.received = invoice.received
        transaction { invoice.payable.txs.forEach { inv.txids.add(TxId(it.hash, it.index)) } }
        return inv
    }

    fun updateStatus(_status: String, systemId: String) {
        transaction { getBySystemId(systemId).status = _status }
    }

    fun updateReceived(_received: BigDecimal, systemId: String) {
        transaction { getBySystemId(systemId).received = _received }
    }
}