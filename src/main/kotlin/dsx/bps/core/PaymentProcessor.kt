package dsx.bps.core

import dsx.bps.core.datamodel.*
import java.math.BigDecimal
import kotlin.concurrent.timer

class PaymentProcessor(private val manager: BlockchainPaymentSystemManager) {

    var frequency: Long = 2000

    // TODO: Implement db-storage for payments
    private val unconfirmed: HashSet<String> = hashSetOf()
    private val payments: HashMap<String, Payment> = hashMapOf()

    init {
        check()
    }

    fun createPayment(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): Payment {
        val pay = Payment(currency, amount, address, tag)
        payments[pay.id] = pay
        unconfirmed.add(pay.id)
        return pay
    }

    fun getPayment(id: String): Payment? = payments[id]

    private fun check() {
        timer(this::class.toString(), true, 0, frequency) {
            unconfirmed
                .mapNotNull { id -> payments[id] }
                .forEach { pay ->
                    val tx = manager.getTx(pay.currency, pay.txid)
                    if (match(pay, tx)) {
                        when (tx.status()){
                            TxStatus.VALIDATING -> {
                                pay.status = PaymentStatus.PROCESSING
                            }
                            TxStatus.CONFIRMED -> {
                                pay.status = PaymentStatus.SUCCEED
                                unconfirmed.remove(pay.id)
                            }
                            TxStatus.REJECTED -> {
                                pay.status = PaymentStatus.FAILED
                                // add this payment to resend list if needed
                            }
                        }
                    } else {
                        // resend payment if needed
                    }
                }
        }
    }

    private fun match(pay: Payment, tx: Tx): Boolean =
            pay.currency == tx.currency() &&
            pay.amount == tx.amount() &&
            pay.address == tx.destination() &&
            pay.tag == tx.tag()
}