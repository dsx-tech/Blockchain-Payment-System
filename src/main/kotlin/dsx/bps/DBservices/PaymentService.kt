package dsx.bps.DBservices

import dsx.bps.DBclasses.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.Type
import dsx.bps.exception.DBservices.BpsDatabaseException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

class PaymentService(datasource: Datasource) {
    init {
        datasource.getConnection()
        transaction {
            if (!PaymentTable.exists())
                SchemaUtils.create(PaymentTable)
            if (!TxTable.exists())
                SchemaUtils.create(TxTable)
        }
    }

    fun add(
        status: PaymentStatus,
        paymentId: String, currency: Currency,
        amount: BigDecimal, address: String,
        tag: Int?
    ): PaymentEntity {
        val newPayment = transaction {
            PaymentEntity.new {
                this.status = status
                this.paymentId = paymentId
                this.currency = currency
                this.amount = amount
                this.address = address
                this.tag = tag
                payable = PayableEntity.new { type = Type.Payment }
            }
        }
        return newPayment
    }

    fun delete(systemId: String) {
        transaction { getBySystemId(systemId).delete() }
    }

    fun getById(id: Int): PaymentEntity? {
        return transaction { PaymentEntity.findById(id) }
    }

    fun getBySystemId(id: String): PaymentEntity {
        return transaction { PaymentEntity.find { PaymentTable.paymentId eq id }.first() }
    }

    fun getStatusedPayments(status: PaymentStatus): MutableList<String> {
        val keys = mutableListOf<String>()
        transaction { PaymentEntity.find { PaymentTable.status eq status }.forEach { keys.add(it.paymentId) } }
        return keys
    }

    fun getPayments(): MutableMap<String, Payment> {
        val paymentMap = mutableMapOf<String, Payment>()
        transaction {
            PaymentEntity.all().forEach {
                paymentMap[it.paymentId] = makePaymentFromDB(it)
            }
        }
        return paymentMap
    }

    fun addTx(systemId: String, tx: TxId) {
        transaction {
            TxEntity.find { TxTable.hash eq tx.hash and (TxTable.index eq tx.index) }.forEach {
                it.payable = getBySystemId(systemId).payable
            }
        }
    }

    fun makePaymentFromDB(payment: PaymentEntity): Payment {
        val pay = Payment(
            payment.paymentId, payment.currency, payment.amount.stripTrailingZeros().add(BigDecimal.ZERO),
            payment.address, payment.tag
        )

        pay.status = payment.status

        transaction {
            if (!payment.payable.txs.empty())
                pay.txid = TxId(payment.payable.txs.first().hash, payment.payable.txs.first().index)
            if (payment.fee != null)
                pay.fee = payment.fee!!
        }
        return pay
    }

    fun updateStatus(status: PaymentStatus, systemId: String) {
        transaction { getBySystemId(systemId).status = status }
    }

    fun updateFee(fee: BigDecimal, systemId: String) {
        transaction { getBySystemId(systemId).fee = fee }
    }
}