package dsx.bps.DBservices.core

import dsx.bps.DBclasses.core.InvoiceEntity
import dsx.bps.DBclasses.core.InvoiceTable
import dsx.bps.DBclasses.core.CryptoAddressEntity
import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.core.tx.TxTable
import dsx.bps.DBservices.Datasource
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
                this.amount = amount
                this.tag = tag
                cryptoAddress = CryptoAddressEntity.new {
                    type = PayableType.Invoice
                    this.address = address
                    this.currency = currency
                }
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

    fun makeInvFromDB(invoice: InvoiceEntity): Invoice {
        return transaction {
            val inv = Invoice(
                invoice.invoiceId, invoice.cryptoAddress.currency, invoice.amount.stripTrailingZeros().add(BigDecimal.ZERO),
                invoice.cryptoAddress.address, invoice.tag
            )
            inv.received = invoice.received
            invoice.cryptoAddress.txs.forEach { inv.txids.add(TxId(it.hash, it.index)) }
            return@transaction inv
        }
    }

    fun updateStatus(status: InvoiceStatus, systemId: String) {
        transaction { getBySystemId(systemId).status = status }
    }

    fun updateReceived(received: BigDecimal, systemId: String) {
        transaction { getBySystemId(systemId).received = received }
    }
}