package dsx.bps.DBservices

import dsx.bps.DBclasses.TxEntity
import dsx.bps.DBclasses.XrpTxEntity
import dsx.bps.DBclasses.XrpTxTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class XrpService(datasource: Datasource) {
    init {
        transaction(datasource.getConnection()) {
            if (!XrpTxTable.exists())
                SchemaUtils.create(XrpTxTable)
        }
    }

    fun add(
        fee: BigDecimal, account: String,
        sequence: Int, validated: Boolean?,
        tx: TxEntity
    ): XrpTxEntity {
        val newXrpTxEntity = transaction {
            XrpTxEntity.new {
                this.fee = fee
                this.account = account
                this.sequence = sequence
                this.validated = validated
                this.tx = tx
            }
        }
        return newXrpTxEntity
    }

    fun delete(xrpTx: XrpTxEntity) {
        transaction { xrpTx.delete() }
    }
}