package dsx.bps.DBclasses.core

import dsx.bps.core.datamodel.Currency
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object DepositAccountTable: IntIdTable() {
    val depositAccountId: Column<String> = varchar("depositAccountId", 100)
}

object EnabledCurrencyTable: IntIdTable() {
    val currency = enumeration("currency", Currency::class)
    val depositAccountId = reference("depositAccountId", DepositAccountTable)
}

object DepositAddressTable: IntIdTable() {
    val cryptoAddressId = reference("cryptoAddressId", CryptoAddressTable)
    val depositAccountId = reference("depositAccountId", DepositAccountTable)
}