package dsx.bps.DBservices

import dsx.bps.DBclasses.*
import dsx.bps.core.datamodel.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class InvoiceService(datasource: Datasource) {
    init {
        transaction(datasource.getConnection()) {
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
        tag: String?
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
                payable = PayableEntity.new { type = PayableType.Invoice }
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