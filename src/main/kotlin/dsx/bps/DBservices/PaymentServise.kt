package dsx.bps.DBservices

import dsx.bps.DBclasses.Payment
import dsx.bps.DBclasses.Payments
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class PaymentService {
    fun create() { // make  static?
        SchemaUtils.create(Payments)
    }

    fun add(_status: String, _fee: Int): Payment {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        val newPayment = transaction{
            Payment.new {
                status = _status
                fee = _fee
            }
        }
        return newPayment
    }

    fun delete(payment: Payment) {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        transaction {payment.delete()}
    }

    fun getById(id: Int): Payment? {
        Database.connect("jdbc:mysql://localhost:3306/exp?serverTimezone=UTC", driver = "com.mysql.jdbc.Driver",
            user = "root", password = "root")
        return transaction { Payment.findById(id)}
    }

    fun updateStatus(_status: String, payment: Payment) {
        payment.status = _status
    }

    fun updateReceived(_fee: Int, payment: Payment) {
        payment.fee = _fee
    }
}