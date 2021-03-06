package dsx.bps.DBclasses.core

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class PaymentEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<PaymentEntity>(PaymentTable)

    var status by PaymentTable.status
    var fee by PaymentTable.fee
    var paymentId by PaymentTable.paymentId
    var amount by PaymentTable.amount
    var tag by PaymentTable.tag
    var cryptoAddress by CryptoAddressEntity referencedOn PaymentTable.cryptoAddressId
}