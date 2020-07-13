package dsx.bps.DBclasses.core

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.PayableType
import org.jetbrains.exposed.dao.IntIdTable

object CryptoAddressTable: IntIdTable() {
    val type = enumeration("type", PayableType::class)
    val address = varchar("address", 100)
    val currency = enumeration("currency", Currency::class)
}