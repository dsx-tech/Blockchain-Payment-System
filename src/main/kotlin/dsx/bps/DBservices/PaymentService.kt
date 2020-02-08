package dsx.bps.DBservices

import dsx.bps.DBclasses.PaymentEntity
import dsx.bps.DBclasses.PaymentTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class PaymentService {
    fun create() {
        Database.connect(Datasource().getHicari())
        transaction { SchemaUtils.create(PaymentTable) }
    }

    fun add(_status: String, _fee: BigDecimal,
            _paymentId: String, _currency: String,
            _amount: BigDecimal, _address: String,
            _tag: Int?): PaymentEntity {
        Database.connect(Datasource().getHicari())
        val newPayment = transaction{
            PaymentEntity.new {
                status = _status
                fee = _fee
                paymentId = _paymentId
                currency = _currency
                amount = _amount
                address = _address
                tag = _tag
            }
        }
        return newPayment
    }

    fun delete(payment: PaymentEntity) {
        Database.connect(Datasource().getHicari())
        transaction {payment.delete()}
    }

    fun getById(id: Int): PaymentEntity? {
        Database.connect(Datasource().getHicari())
        return transaction { PaymentEntity.findById(id)}
    }

    fun getBySystemId(id: String): PaymentEntity {
        Database.connect(Datasource().getHicari())// url как поле класса в конструкторе и конфиге
        return transaction {PaymentEntity.find {PaymentTable.paymentId eq id}.first()}
    }

    fun updateStatus(_status: String, payment: PaymentEntity) {
        Database.connect(Datasource().getHicari())
        transaction { payment.status = _status }
    }

    fun updateFee(_fee: BigDecimal, payment: PaymentEntity) {
        Database.connect(Datasource().getHicari())
        transaction { payment.fee = _fee }
    }
}