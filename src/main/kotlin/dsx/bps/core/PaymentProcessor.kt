package dsx.bps.core

import com.uchuhimo.konf.Config
import dsx.bps.config.PaymentProcessorConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.exception.core.payment.PaymentException
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.timer

class PaymentProcessor(private val manager: BlockchainPaymentSystemManager, config: Config) {

    var frequency: Long = config[PaymentProcessorConfig.frequency]

    // TODO: Implement db-storage for payments
    private val pending = ConcurrentHashMap.newKeySet<String>()
    private val processing = ConcurrentHashMap.newKeySet<String>()
    private val payments = ConcurrentHashMap<String, Payment>()

    init {
        check()
    }

    fun createPayment(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): Payment {
        val id = UUID.randomUUID().toString().replace("-", "")
        val pay = Payment(id, currency, amount, address, tag)
        payments[pay.id] = pay
        pending.add(pay.id)
        return pay
    }

    fun updatePayment(id: String, tx: Tx) {
        if (!pending.contains(id)) throw PaymentException("There is no pending payment with id = $id")
        val payment = payments[id]
                      ?: throw PaymentException("There is no payment with id = $id")

        payment.txid = tx.txid()
        payment.fee = tx.fee()

        pending.remove(id)
        payment.status = PaymentStatus.PROCESSING
        processing.add(id)
    }

    fun getPayment(id: String): Payment? = payments[id]

    private fun check() {
        timer(this::class.toString(), true, 0, frequency) {
            processing
                .mapNotNull { id -> payments[id] }
                .forEach { pay ->
                    val tx = manager.getTx(pay.currency, pay.txid)
                    if (match(pay, tx)) {
                        when (tx.status()) {
                            TxStatus.VALIDATING -> {
                                pay.status = PaymentStatus.PROCESSING
                            }
                            TxStatus.CONFIRMED  -> {
                                pay.status = PaymentStatus.SUCCEED
                                processing.remove(pay.id)
                            }
                            TxStatus.REJECTED   -> {
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
        pay.amount.compareTo(tx.amount()) == 0 &&
        pay.address == tx.destination() &&
        pay.tag == tx.tag()
}