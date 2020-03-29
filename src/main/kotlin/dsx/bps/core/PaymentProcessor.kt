package dsx.bps.core

import com.uchuhimo.konf.Config
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.PaymentService
import dsx.bps.config.PaymentProcessorConfig
import dsx.bps.core.datamodel.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.exception.core.payment.PaymentException
import java.math.BigDecimal
import java.util.*
import kotlin.concurrent.timer

class PaymentProcessor(private val manager: BlockchainPaymentSystemManager, config: Config, datasource: Datasource) {

    var frequency: Long = config[PaymentProcessorConfig.frequency]

    private val payService = PaymentService(datasource)
    private val pending = payService.getStatusedPayments(PaymentStatus.PENDING)
    private val processing = payService.getStatusedPayments(PaymentStatus.PROCESSING)
    private val payments = payService.getPayments()

    init {
        check()
    }

    fun createPayment(currency: Currency, amount: BigDecimal, address: String, tag: String? = null): Payment {
        val id = UUID.randomUUID().toString().replace("-", "")
        val pay = Payment(id, currency, amount, address, tag)
        payService.add(PaymentStatus.PENDING, id, currency, amount, address, tag)
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

        payService.updateStatus(PaymentStatus.PROCESSING, id)
        payService.updateFee(payment.fee, id)
        payService.addTx(id, tx.txid())
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
                                payService.updateStatus(PaymentStatus.PROCESSING, pay.id)
                                pay.status = PaymentStatus.PROCESSING
                            }
                            TxStatus.CONFIRMED -> {
                                payService.updateStatus(PaymentStatus.SUCCEED, pay.id)
                                pay.status = PaymentStatus.SUCCEED
                                processing.remove(pay.id)
                            }
                            TxStatus.REJECTED -> {
                                payService.updateStatus(PaymentStatus.FAILED, pay.id)
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
                pay.tag == tx.paymentReference()
}