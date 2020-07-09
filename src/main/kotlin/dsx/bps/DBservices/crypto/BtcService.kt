package dsx.bps.DBservices.crypto

import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.crypto.btc.BtcTxEntity
import org.jetbrains.exposed.sql.transactions.transaction

class BtcService {

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