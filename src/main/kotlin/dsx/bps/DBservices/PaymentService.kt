package dsx.bps.DBservices

import dsx.bps.DBclasses.PaymentEntity
import dsx.bps.DBclasses.PaymentTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class PaymentService {
    fun create() {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction { SchemaUtils.create(PaymentTable) }
    }

    fun add(_status: String, _fee: String,
            _paymentId: String, _currency: String,
            _amount: String, _address: String,
            _tag: Int?): PaymentEntity {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
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
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction {payment.delete()}
    }

    fun getById(id: Int): PaymentEntity? {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        return transaction { PaymentEntity.findById(id)}
    }

    fun updateStatus(_status: String, payment: PaymentEntity) {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction { payment.status = _status }
    }

    fun updateFee(_fee: String, payment: PaymentEntity) {
        Database.connect("jdbc:mysql://localhost:3306/inv?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction { payment.fee = _fee }
    }
}