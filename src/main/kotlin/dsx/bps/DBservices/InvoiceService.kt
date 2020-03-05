package dsx.bps.DBservices

import dsx.bps.DBclasses.*
import dsx.bps.DBclasses.PayableEntity
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Invoice
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.Type
import dsx.bps.exception.DBservices.BpsDatabaseException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

class InvoiceService(datasource: Datasource) {
    init {
        datasource.getConnection()
        transaction {
            if (!InvoiceTable.exists())
                SchemaUtils.create(InvoiceTable)
            if (!TxTable.exists())
                SchemaUtils.create(TxTable)
        }
    }

    fun add(
        status: InvoiceStatus, received: BigDecimal,
        invoiceId: String, currency: Currency,
        amount: BigDecimal, address: String,
        tag: Int?
    ): InvoiceEntity {
        val newInvoice = transaction {
            InvoiceEntity.new {
                this.status = status
                this.received = received
                this.invoiceId = invoiceId
                this.currency = currency
                this.amount = amount
                this.address = address
                this.tag = tag
                payable = PayableEntity.new { type = Type.Invoice }
            }
        }

        return newInvoice
    }

    fun delete(systemId: String) {
        transaction { getBySystemId(systemId).delete() }
    }

    fun getById(id: Int): InvoiceEntity? {
        return transaction { InvoiceEntity.findById(id) }
    }

    fun getBySystemId(id: String): InvoiceEntity {
        return transaction { InvoiceEntity.find { InvoiceTable.invoiceId eq id }.first() }
    }

    fun getAll(): SizedIterable<InvoiceEntity> {
        return InvoiceEntity.all()
    }

    fun getUnpaid(): MutableList<String> {
        val unpaid = mutableListOf<String>()
        transaction {
            InvoiceEntity.find { InvoiceTable.status eq InvoiceStatus.UNPAID }.forEach { unpaid.add(it.invoiceId) }
        }
        return unpaid
    }

    fun getInvoices(): MutableMap<String, Invoice> {
        val invoiceMap = mutableMapOf<String, Invoice>()
        transaction {
            InvoiceEntity.all().forEach {
                invoiceMap[it.invoiceId] = makeInvFromDB(it)
            }
        }
        return invoiceMap

    }

    fun addTx(systemId: String, tx: TxId) {
        transaction {
            TxEntity.find { TxTable.hash eq tx.hash and (TxTable.index eq tx.index) }.forEach {
                it.payable = getBySystemId(systemId).payable
            }
        }
    }

    fun makeInvFromDB(invoice: InvoiceEntity): Invoice {
        val inv = Invoice(
            invoice.invoiceId, invoice.currency, invoice.amount.stripTrailingZeros().add(BigDecimal.ZERO),
            invoice.address, invoice.tag
        )
        inv.received = invoice.received
        transaction { invoice.payable.txs.forEach { inv.txids.add(TxId(it.hash, it.index)) } }
        return inv
    }

    fun updateStatus(status: InvoiceStatus, systemId: String) {
        transaction { getBySystemId(systemId).status = status }
    }

    fun updateReceived(received: BigDecimal, systemId: String) {
        transaction { getBySystemId(systemId).received = received }
    }
}