package dsx.bps.DBservices

import dsx.bps.DBclasses.core.TxEntity
import dsx.bps.DBclasses.crypto.btc.BtcTxEntity
import dsx.bps.DBclasses.crypto.btc.BtcTxTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

class BtcService(datasource: Datasource) {
    init {
        transaction(datasource.getConnection()) {
            if (!BtcTxTable.exists())
                SchemaUtils.create(BtcTxTable)
        }
    }

    fun add(
        confirmations: Int, address: String,
        tx: TxEntity
    ): BtcTxEntity {
        val newBtcTxEntity = transaction {
            BtcTxEntity.new {
                this.confirmations = confirmations
                this.address = address
                this.tx = tx
            }
        }
        return newBtcTxEntity
    }

    fun delete(btcTx: BtcTxEntity) {
        transaction { btcTx.delete() }
    }
}