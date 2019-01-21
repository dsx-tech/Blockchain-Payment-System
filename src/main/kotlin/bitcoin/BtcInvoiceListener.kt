package dsx.bps.kotlin.bitcoin

import dsx.bps.kotlin.core.InvoiceListener
import dsx.bps.kotlin.core.InvoiceStatus
import java.math.BigDecimal
import java.util.*
import kotlin.concurrent.timer

class BtcInvoiceListener(var rpc: BtcRPC): InvoiceListener() {

    var frequency: Long = 5000 // 5 sec

    init {
        timer("BtcInvoinceListener", true, 0, frequency) {
            unpaidInvoices.onEach { entry ->
                val inv = entry.value
                val recieved = BigDecimal.ZERO
                for (txId in inv.txIds) {
                    val tx = rpc.getTransaction(txId)
                }
            }
        }
    }

    override fun update(o: Observable?, arg: Any?) {

        if (arg !is BtcJSON.BtcBlock)
            return

        if (unpaidInvoices.isEmpty())
            return

        for (tx in arg.tx) {
            for (vout in tx.vout) {

                val value = vout.value
                val addresses = vout.scriptPubKey.addresses ?: continue
                val address = addresses.firstOrNull() ?: continue

                unpaidInvoices.forEach { entry ->
                    val inv = entry.value
                    if (inv.address == address) {
                        inv.txIds.add(tx.txid)
                        println("BtcInvoiceListener: new payment $value btc to $address")
                        if (inv.status == InvoiceStatus.PAID) {
                            println("invoice ${inv.id}: {${inv.address} | ${inv.amount}} has been paid in tx ${tx.txid}")
                        }
                    }
                }
            }
        }
    }
}