package dsx.bps.DBservices.core

import dsx.bps.DBclasses.core.CryptoAddressEntity
import dsx.bps.DBclasses.core.PaymentEntity
import dsx.bps.DBclasses.core.PaymentTable
import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.core.tx.TxTable
import dsx.bps.DBservices.Datasource
import dsx.bps.core.datamodel.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class PaymentService(datasource: Datasource) {
    init {
        transaction(datasource.getConnection()) {
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
        tag: String?
    ): PaymentEntity {
        val newPayment = transaction {
            PaymentEntity.new {
                this.status = status
                this.paymentId = paymentId
                this.amount = amount
                this.tag = tag
                cryptoAddress = CryptoAddressEntity.new {
                    type = PayableType.Payment
                    this.address = address
                    this.currency = currency
                }
            }
        }
        return newPayment
    }

    fun delete(systemId: String) {
        transaction { getBySystemId(systemId).delete() }
    }

    fun deleteAll() {
        transaction { PaymentTable.deleteAll() }
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

    fun makePaymentFromDB(payment: PaymentEntity): Payment {
        return transaction {
            val pay = Payment(
                payment.paymentId, payment.cryptoAddress.currency, payment.amount.stripTrailingZeros().add(BigDecimal.ZERO),
                payment.cryptoAddress.address, payment.tag
            )

            pay.status = payment.status


            if (!payment.cryptoAddress.txs.empty())
                pay.txid = TxId(payment.cryptoAddress.txs.first().hash, payment.cryptoAddress.txs.first().index)
            if (payment.fee != null)
                pay.fee = payment.fee!!

            return@transaction pay
        }
    }

    fun updateStatus(status: PaymentStatus, systemId: String) {
        transaction { getBySystemId(systemId).status = status }
    }

    fun updateFee(fee: BigDecimal, systemId: String) {
        transaction { getBySystemId(systemId).fee = fee }
    }
}