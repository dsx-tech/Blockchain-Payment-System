package dsx.bps.DBclasses.core

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class DepositAccountEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<DepositAccountEntity>(DepositAccountTable)

    var depositAccountId by DepositAccountTable.depositAccountId
    val enabledCurrency by EnabledCurrencyEntity referrersOn EnabledCurrencyTable.depositAccountTable
    val depositAddress by DepositAddressEntity referrersOn DepositAddressTable.depositAccountTable
}

class EnabledCurrencyEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<EnabledCurrencyEntity>(EnabledCurrencyTable)

    var currency by EnabledCurrencyTable.currency
    var depositAccount by DepositAccountEntity referencedOn EnabledCurrencyTable.depositAccountTable
}

class DepositAddressEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<DepositAddressEntity>(DepositAddressTable)

    var cryptoAddress by CryptoAddressEntity referencedOn DepositAddressTable.cryptoAddressTable
    var depositAccount by DepositAccountEntity referencedOn DepositAddressTable.depositAccountTable
}
