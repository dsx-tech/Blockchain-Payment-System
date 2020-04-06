package dsx.bps.DBservices

import dsx.bps.DBclasses.EthTxEntity
import dsx.bps.DBclasses.EthTxTable
import dsx.bps.DBclasses.TxEntity
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigInteger

class EthService(datasource: Datasource) {
    init {
        transaction(datasource.getConnection()) {
            if (!EthTxTable.exists())
                SchemaUtils.create(EthTxTable)
        }
    }

    fun add(address: String, nonce: Long,
        tx: TxEntity
    ): EthTxEntity {
        val newEthTxEntity = transaction {
            EthTxEntity.new {
                this.address = address
                this.nonce = nonce
                this.tx = tx
            }
        }
        return newEthTxEntity
    }

    fun getLatestNonce(address: String) : BigInteger?
    {
        val nonce = transaction{ EthTxEntity.all().filter { tx -> tx.address == address }.map { tx -> tx.nonce}.max()}
        return nonce?.toBigInteger() ?: return null
    }

    fun delete() {
        transaction { EthTxEntity.all() .forEach {it.delete()} }
    }
}