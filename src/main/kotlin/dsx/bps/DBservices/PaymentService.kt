package dsx.bps.DBservices

import dsx.bps.DBclasses.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.core.datamodel.TxId
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

class PaymentService {
    init {
        Database.connect(Datasource().getHicari())
        transaction {
            if (!PaymentTable.exists())
                SchemaUtils.create(PaymentTable)
            if (!TxTable.exists())
                SchemaUtils.create(TxTable)
        }
    }

    fun add(_status: String,
            _paymentId: String, _currency: String,
            _amount: BigDecimal, _address: String,
            _tag: Int?): PaymentEntity {
        Database.connect(Datasource().getHicari())
        val newPayment = transaction{
            PaymentEntity.new {
                status = _status
                paymentId = _paymentId
                currency = _currency
                amount = _amount
                address = _address
                tag = _tag
                payable = PayableEntity.new { type = "payment" }
            }
        }
        return newPayment
    }

    fun delete(systemId: String) {
        Database.connect(Datasource().getHicari())
        transaction {getBySystemId(systemId).delete()}
    }

    fun getById(id: Int): PaymentEntity? {
        Database.connect(Datasource().getHicari())
        return transaction { PaymentEntity.findById(id)}
    }

    fun getBySystemId(id: String): PaymentEntity {
        Database.connect(Datasource().getHicari())// url как поле класса в конструкторе и конфиге
        return transaction {PaymentEntity.find {PaymentTable.paymentId eq id}.first()}
    }

    fun getStatusedPayments(status: String): ConcurrentHashMap.KeySetView<String, Boolean> {
        Database.connect(Datasource().getHicari())
        val payments = transaction { PaymentEntity.find {PaymentTable.status eq status} }
        val keys = ConcurrentHashMap.newKeySet<String>()
        transaction { payments.forEach { keys.add(it.paymentId) } }
        return keys
    }

    fun getPayments(): ConcurrentHashMap<String, Payment> {
        Database.connect(Datasource().getHicari())
        val paymentMap = ConcurrentHashMap<String, Payment>()
        transaction {
            PaymentEntity.all().forEach {
                paymentMap[it.paymentId] =  makePaymentFromDB(it)
            }
        }

        return paymentMap
    }

    fun addTx(systemId: String,tx: TxId) {
        Database.connect(Datasource().getHicari())
        transaction {
            TxEntity.find { TxTable.hash eq tx.hash and (TxTable.index eq tx.index)}.forEach {
                it.payable = getBySystemId(systemId).payable
            }
        }
    }

    fun makePaymentFromDB (payment: PaymentEntity): Payment {
        val currency: Currency
        currency = when (payment.currency){
            "BTC" -> Currency.BTC
            "XRP" -> Currency.XRP
            else -> Currency.TRX
        }
        val pay = Payment(payment.paymentId, currency, payment.amount.stripTrailingZeros().add(BigDecimal.ZERO),
                          payment.address, payment.tag)
        pay.status = when (payment.status){
            "pending" -> PaymentStatus.PENDING
            "processing" -> PaymentStatus.PROCESSING
            "succeed" -> PaymentStatus.SUCCEED
            else -> PaymentStatus.FAILED
        }
        if (!payment.payable.txs.empty())
            pay.txid = TxId (payment.payable.txs.first().hash, payment.payable.txs.first().index)
        if (payment.fee != null)
            pay.fee = payment.fee!!

        return pay
    }

    fun updateStatus(_status: String, systemId: String) {
        Database.connect(Datasource().getHicari())
        transaction { getBySystemId(systemId).status = _status }
    }

    fun updateFee(_fee: BigDecimal, systemId: String) {
        Database.connect(Datasource().getHicari())
        transaction { getBySystemId(systemId).fee = _fee }
    }
}