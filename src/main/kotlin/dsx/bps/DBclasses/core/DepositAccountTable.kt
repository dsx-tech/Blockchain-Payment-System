package dsx.bps.DBclasses.core

import dsx.bps.core.datamodel.Currency
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object DepositAccountTable: IntIdTable() {
    val depositAccountId: Column<String> = varchar("depositAccountId", 100)
    val payableId = reference("payableId", PayableTable)
}

object EnabledCurrencyTable: IntIdTable() {
    val currency = enumeration("currency", Currency::class)
    val depositAccountTable = reference("depositAccountTable", DepositAccountTable)
}

object AddressTable: IntIdTable() {
    val currency = enumeration("currency", Currency::class)
    val address: Column<String> = varchar("address", 100)
    val depositAccountTable = reference("depositAccountTable", DepositAccountTable)
}