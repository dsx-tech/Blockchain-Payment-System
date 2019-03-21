package dsx.bps.core

import dsx.bps.core.datamodel.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.math.BigDecimal
import java.util.UUID

class InvoiceProcessor(private val manager: BlockchainPaymentSystemManager): Observer<Tx> {

    // TODO: Implement db-storage for invoices
    private val unpaid: HashSet<String> = hashSetOf()
    private val invoices: HashMap<String, Invoice> = hashMapOf()

    init {
        manager.subscribe(this)
    }

    fun getInvoice(id: String): Invoice? = invoices[id]

    fun createInvoice(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): Invoice {
        val id = UUID.randomUUID().toString().replace("-", "")
        val inv = Invoice(id, currency, amount, address, tag)
        invoices[inv.id] = inv
        unpaid.add(inv.id)
        return inv
    }

    private fun recalculate(inv: Invoice) {
        var received = BigDecimal.ZERO
        manager
            .getTxs(inv.currency, inv.txids)
            .forEach { tx ->
                when (tx.status()) {
                    TxStatus.REJECTED ->
                        inv.txids.remove(tx.txid())
                    TxStatus.CONFIRMED ->
                        received += tx.amount()
                    else -> {}
                }
            }
        inv.received = received
    }

    private fun match(inv: Invoice, tx: Tx): Boolean =
        inv.currency == tx.currency() &&
        inv.address == tx.destination() &&
        inv.tag == tx.tag()

    /** Check for payment in transaction [tx] */
    override fun onNext(tx: Tx) {
        if (unpaid.isEmpty())
            return

        unpaid
            .mapNotNull { id -> invoices[id] }
            .filter { inv -> match(inv, tx) }
            .forEach { inv ->
                recalculate(inv)

                inv.txids.add(tx.txid())

                if (tx.status() == TxStatus.CONFIRMED)
                    inv.received += tx.amount()

                if (inv.status == InvoiceStatus.PAID)
                    unpaid.remove(inv.id)
            }
    }

    override fun onError(e: Throwable) {
        println(e.message + ":\n" + e.stackTrace)
    }

    override fun onComplete() {}

    override fun onSubscribe(d: Disposable) {}
}