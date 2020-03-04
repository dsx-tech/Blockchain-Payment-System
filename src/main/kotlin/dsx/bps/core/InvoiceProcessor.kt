package dsx.bps.core

import com.uchuhimo.konf.Config
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.InvoiceService
import dsx.bps.DBservices.TxService
import dsx.bps.config.InvoiceProcessorConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Invoice
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxStatus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.math.BigDecimal
import java.util.UUID
import kotlin.concurrent.timer

class InvoiceProcessor(private val manager: BlockchainPaymentSystemManager, config: Config, datasource: Datasource): Observer<Tx> {

    private val invService = InvoiceService(datasource)
    private val txService = TxService(datasource)
    private val unpaid = invService.getUnpaid()
    private val invoices = invService.getInvoices()

    var frequency: Long = config[InvoiceProcessorConfig.frequency]

    init {
        manager.subscribe(this)
        check()
    }

    fun createInvoice(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): Invoice {
        val id = UUID.randomUUID().toString().replace("-", "")
        val inv = Invoice(id, currency, amount, address, tag)
        invService.add("unpaid", BigDecimal.ZERO, id, currency, amount, address, tag)
        invoices[inv.id] = inv
        unpaid.add(inv.id)
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
                    if (tx.status() == TxStatus.REJECTED) {
                        txService.updateStatus("REJECTED", tx.hash(), tx.index())
                        inv.txids.remove(tx.txid())
                    } else if (tx.status() == TxStatus.CONFIRMED) {
                        txService.updateStatus("CONFIRMED", tx.hash(), tx.index())
                        received += tx.amount()
                    }
                }
        }
        invService.updateReceived(received, inv.id)
        if (inv.status == InvoiceStatus.PAID)
            invService.updateStatus("paid", inv.id)
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

                invService.addTx(inv.id, tx.txid())
                inv.txids.add(tx.txid())

                if (tx.status() == TxStatus.CONFIRMED) {
                    invService.updateReceived(inv.received + tx.amount(), inv.id)
                    inv.received += tx.amount()
                }

                if (inv.status == InvoiceStatus.PAID) {
                    invService.updateStatus("paid", inv.id)
                    unpaid.remove(inv.id)
                }
            }
    }

    override fun onError(e: Throwable) {
        println(e.message + ":\n" + e.stackTrace)
    }

    override fun onComplete() {}

    override fun onSubscribe(d: Disposable) {}
}