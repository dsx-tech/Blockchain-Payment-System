package dsx.bps.DBclasses.core

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class DepositAccountEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<DepositAccountEntity>(DepositAccountTable)

    var depositAccountId by DepositAccountTable.depositAccountId
    val enabledCurrency by EnabledCurrencyEntity referrersOn EnabledCurrencyTable.depositAccountId
    val depositAddress by DepositAddressEntity referrersOn DepositAddressTable.depositAccountId
}

class EnabledCurrencyEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<EnabledCurrencyEntity>(EnabledCurrencyTable)

    var currency by EnabledCurrencyTable.currency
    var depositAccount by DepositAccountEntity referencedOn EnabledCurrencyTable.depositAccountId
}

class DepositAddressEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<DepositAddressEntity>(DepositAddressTable)

    var cryptoAddress by CryptoAddressEntity referencedOn DepositAddressTable.cryptoAddressId
    var depositAccount by DepositAccountEntity referencedOn DepositAddressTable.depositAccountId
}
