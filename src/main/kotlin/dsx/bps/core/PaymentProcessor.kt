package dsx.bps.core

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.Tx
import java.math.BigDecimal

class PaymentProcessor(private val manager: BlockchainPaymentSystemManager) {

    var frequency: Long = 5000

    // Default value for now
    private var confirmations = 1

    // TODO: Implement db-storage for payments
    private val payments: HashMap<String, Payment> = hashMapOf()

    fun createPayment(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): Payment {
        val pay = Payment(currency, amount, address, tag)
        payments[pay.id] = pay
        return pay
    }

    fun getPayment(id: String): Payment? = payments[id]
}