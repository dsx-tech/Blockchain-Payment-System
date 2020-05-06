package dsx.bps.DBclasses.core

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class DepositAccountEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<DepositAccountEntity>(DepositAccountTable)

    var depositAccountId by DepositAccountTable.depositAccountId
    val enabledCurrency by EnabledCurrencyEntity referrersOn EnabledCurrencyTable.depositAccountTable
    val address by AddressEntity referrersOn AddressTable.depositAccountTable
    var payable by PayableEntity referencedOn DepositAccountTable.payableId
}

class EnabledCurrencyEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<EnabledCurrencyEntity>(EnabledCurrencyTable)

    var currency by EnabledCurrencyTable.currency
    var depositAccount by DepositAccountEntity referencedOn EnabledCurrencyTable.depositAccountTable
}

class AddressEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<AddressEntity>(AddressTable)

    var currency by AddressTable.currency
    var address by AddressTable.address
    var depositAccount by DepositAccountEntity referencedOn AddressTable.depositAccountTable
}
