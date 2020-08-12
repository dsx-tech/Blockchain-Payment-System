package dsx.bps.DBservices.crypto

import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.crypto.xrp.XrpTxEntity
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class XrpService {

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