package dsx.bps.core

import com.uchuhimo.konf.Config
import dsx.bps.DBservices.InvoiceService
import dsx.bps.config.DatabaseConfig
import dsx.bps.config.InvoiceProcessorConfig
import dsx.bps.core.datamodel.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.timer

class InvoiceProcessor(private val manager: BlockchainPaymentSystemManager, config: Config): Observer<Tx> {

    // TODO: Implement db-storage for invoices
    private val invService = InvoiceService()
    private val unpaid = invService.getUnpaid()
    private val invoices = invService.getInvoices()
    //private val unpaid = ConcurrentHashMap.newKeySet<String>()
    //private val invoices = ConcurrentHashMap<String, Invoice>()

    var frequency: Long = config[InvoiceProcessorConfig.frequency]

    init {
        manager.subscribe(this)
        check()
    }

    fun createInvoice(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): Invoice {
        val id = UUID.randomUUID().toString().replace("-", "")
        val inv = Invoice(id, currency, amount, address, tag)
        invoices[inv.id] = inv
        unpaid.add(inv.id)//make atomic or recalculate error
        invService.add("unpaid", BigDecimal.ZERO, id, currency.toString(), amount, address, tag)
        return inv
    }

    fun getInvoice(id: String): Invoice? = invoices[id]

    private fun check() {
        timer(this::class.toString(), true, 0, frequency) {
            unpaid
                .mapNotNull { invoices[it] }
                .forEach { recalculate(it) }
        }
    }

    private fun recalculate(inv: Invoice) {
        var received = BigDecimal.ZERO
        synchronized(inv.txids) {
            manager
                .getTxs(inv.currency, inv.txids)
                .forEach { tx ->
                    when (tx.status()) {
                        TxStatus.REJECTED ->
                            inv.txids.remove(tx.txid()) // do we need to store rejected transactions in db?
                        TxStatus.CONFIRMED ->
                            received += tx.amount()
                        else -> {
                        }
                    }
                }
        }
        inv.received = received // why not remove from unpaid if needed?
        invService.updateReceived(received, inv.id)
        if (inv.status == InvoiceStatus.PAID)
            invService.updateStatus("paid", inv.id)
    }

    private fun match(inv: Invoice, tx: Tx): Boolean =
        inv.currency == tx.currency() &&
        inv.address == tx.destination() &&
        inv.tag == tx.tag()

    /** Check for payment in transaction [tx] */
    override fun onNext(tx: Tx) {//need to recalculate if failed?
        if (unpaid.isEmpty())
            return

        unpaid
            .mapNotNull { id -> invoices[id] }
            .filter { inv -> match(inv, tx) }
            .forEach { inv ->
                recalculate(inv)

                inv.txids.add(tx.txid())
                invService.addTx(inv.id, tx.txid())

                if (tx.status() == TxStatus.CONFIRMED) {
                    inv.received += tx.amount()
                    invService.updateReceived(inv.received, inv.id)
                }

                if (inv.status == InvoiceStatus.PAID) {
                    unpaid.remove(inv.id)
                    invService.updateStatus("paid", inv.id)
                }
            }
    }

    override fun onError(e: Throwable) {
        println(e.message + ":\n" + e.stackTrace)
    }

    override fun onComplete() {}

    override fun onSubscribe(d: Disposable) {}
}