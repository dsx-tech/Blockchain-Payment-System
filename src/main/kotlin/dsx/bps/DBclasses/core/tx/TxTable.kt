package dsx.bps.DBclasses.core.tx

import dsx.bps.DBclasses.core.CryptoAddressTable
import dsx.bps.DBclasses.core.TableConstants.tagMaxLength
import dsx.bps.core.datamodel.TxStatus
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object TxTable: IntIdTable() {
    val status = enumeration("status", TxStatus::class)
    val tag: Column<String?> = varchar("tag", tagMaxLength).nullable()
    val amount: Column<BigDecimal> = decimal("amount", 30, 15)
    val fee: Column<BigDecimal> = decimal("fee", 30, 15)
    val hash: Column<String> = varchar("hash", 500)
    val index: Column<Long> = long("index")
    val cryptoAddressId = reference("cryptoAddressId", CryptoAddressTable)
}