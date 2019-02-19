package dsx.bps.crypto.btc

import java.math.BigDecimal
import java.util.Observable
import dsx.bps.core.InvoiceStatus
import dsx.bps.crypto.InvoiceListener

class BtcInvoiceListener(var rpc: BtcRPC): InvoiceListener() {

    var confirmations: Int = 1

    override fun update(o: Observable?, arg: Any?) {

        if (unpaidInvoices.isEmpty())
            return

        /* recalculation of already received funds */
        unpaidInvoices.forEach { _, inv ->
            var received = BigDecimal.ZERO

            inv.txIds
                .map { rpc.getTransaction(it) }
                .forEach { tx ->
                    when {
                        tx.confirmations < 0 ->
                            inv.txIds.remove(tx.txid)
                        tx.confirmations >= confirmations ->
                            tx.details.forEach { detail ->
                                if (detail.address == inv.address &&
                                    detail.category == "receive") {
                                    received += detail.amount
                                }
                            }
                    }
                }
            inv.received = received
        }

        /* collection of new received funds*/
        if (arg !is List<*>)
            return

        for (tx in arg) {
            if (tx !is BtcJSON.BtcTxSinceBlock)
                continue

            unpaidInvoices.forEach { _, inv ->
                if (tx.address == inv.address &&
                    tx.category == "receive") {
                    inv.txIds.add(tx.txid)
                    if (tx.confirmations >= confirmations) {
                        inv.received += tx.amount
                    }
                }
            }
        }

        /* filtering unpaid invoices */
        unpaidInvoices.entries.removeAll { entry ->
            val inv = entry.value
            if (inv.status == InvoiceStatus.PAID)
                println("invoice ${inv.id}: { ${inv.address} | ${inv.amount} } has been paid in txs: [ ${inv.txIds.joinToString()} ]")

            inv.status == InvoiceStatus.PAID }
    }
}