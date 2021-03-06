package dsx.bps.core

import com.uchuhimo.konf.Config
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.InvoiceService
import dsx.bps.DBservices.core.TxService
import dsx.bps.config.InvoiceProcessorConfig
import dsx.bps.core.datamodel.*
import dsx.bps.core.datamodel.Currency
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.timer

class InvoiceProcessor(
    private val manager: BlockchainPaymentSystemManager,
    config: Config, txServ: TxService
) : Observer<Tx> {

    private val invService = InvoiceService()
    private val txService = txServ
    private val unpaid: ConcurrentLinkedQueue<String> = ConcurrentLinkedQueue(invService.getUnpaid())
    private val invoices = invService.getInvoices()

    var frequency: Long = config[InvoiceProcessorConfig.frequency]

    init {
        manager.subscribe(this)
        check()
    }

    fun createInvoice(currency: Currency, amount: BigDecimal, address: String, tag: String? = null): Invoice {
        synchronized(unpaid)
        {
            val id = UUID.randomUUID().toString().replace("-", "")
            val inv = Invoice(id, currency, amount, address, tag)
            invService.add(InvoiceStatus.UNPAID, BigDecimal.ZERO, id, currency, amount, address, tag)
            invoices[inv.id] = inv
            unpaid.add(inv.id)
            return inv
        }
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
        synchronized(inv) {
            manager
                .getTxs(inv.currency, inv.txids)
                .forEach { tx ->
                    if (tx.status() == TxStatus.REJECTED) {
                        txService.updateStatus(TxStatus.REJECTED, tx.hash(), tx.index())
                        inv.txids.remove(tx.txid())
                    } else if (tx.status() == TxStatus.CONFIRMED) {
                        txService.updateStatus(TxStatus.CONFIRMED, tx.hash(), tx.index())
                        received += tx.amount()
                    }
                }

            invService.updateReceived(received, inv.id)
            if (inv.status == InvoiceStatus.PAID)
                invService.updateStatus(InvoiceStatus.PAID, inv.id)
            inv.received = received
        }
    }

    private fun match(inv: Invoice, tx: Tx): Boolean =
        inv.currency == tx.currency() &&
        inv.address == tx.destination() &&
                inv.tag == tx.paymentReference()

    /** Check for payment in transaction [tx] */
    override fun onNext(tx: Tx) {
        if (unpaid.isEmpty())
            return

        unpaid
            .mapNotNull { id -> invoices[id] }
            .filter { inv -> match(inv, tx) }
            .forEach { inv ->
                recalculate(inv)

                synchronized(inv) {
                    inv.txids.add(tx.txid())

                    if (tx.status() == TxStatus.CONFIRMED) {
                        invService.updateReceived(inv.received + tx.amount(), inv.id)
                        inv.received += tx.amount()
                    }

                    if (inv.status == InvoiceStatus.PAID) {
                        invService.updateStatus(InvoiceStatus.PAID, inv.id)
                        unpaid.remove(inv.id)
                    }
                }
            }
    }

    override fun onError(e: Throwable) {
        println(e.message + ":\n" + e.stackTrace)
    }

    override fun onComplete() {}

    override fun onSubscribe(d: Disposable) {}
}