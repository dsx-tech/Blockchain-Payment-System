package dsx.bps.DBservices.core

import dsx.bps.DBclasses.core.CryptoAddressEntity
import dsx.bps.DBclasses.core.CryptoAddressTable
import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.core.tx.TxTable
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.PayableType
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxStatus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class TxService {

    fun add(
        status: TxStatus, destination: String,
        tag: String?, amount: BigDecimal,
        fee: BigDecimal, hash: String,
        index: Long, currency: Currency
    ): TxEntity {
        val newTx = transaction {
            TxEntity.new {
                this.status = status
                this.tag = tag
                this.amount = amount
                this.fee = fee
                this.hash = hash
                this.index = index
                /*cryptoAddress = CryptoAddressEntity.find {
                    CryptoAddressTable.currency eq currency and (CryptoAddressTable.address eq destination)
                }.first() */
                val addresses = transaction { CryptoAddressEntity.all().filter { it.currency == currency
                        && it.address == destination }}
                if (addresses.count() == 0)
                {
                    this.cryptoAddress = CryptoAddressEntity.new {
                        type = PayableType.Payment
                        this.address = destination
                        this.currency = currency
                    }
                }
                else
                {
                    this.cryptoAddress = addresses.first()
                }
            }
        }
        return newTx
    }

    fun checkCryptoAddress(tx: Tx): Boolean {
        return transaction {
            !CryptoAddressEntity.find {
                CryptoAddressTable.currency eq tx.currency() and (CryptoAddressTable.address eq tx.destination()) }.empty()
        }
    }

    fun getById(id: Int): TxEntity? {
        return transaction { TxEntity.findById(id) }
    }

    fun getByTxId(hash: String, index: Long): TxEntity {
        return transaction { TxEntity.find { TxTable.hash eq hash and (TxTable.index eq index) }.first() }
    }

    fun getCurrency(txEntity: TxEntity): Currency {
        return transaction { txEntity.cryptoAddress.currency }
    }

    fun getDestination(txEntity: TxEntity): String {
        return transaction { txEntity.cryptoAddress.address }
    }

    fun updateStatus(status: TxStatus, hash: String, index: Long) {
        return transaction { getByTxId(hash, index).status = status }
    }

    fun constructTxByTxEntity(txEntity: TxEntity): Tx {
        return object : Tx {
            override fun currency(): Currency = getCurrency(txEntity)
            override fun hash(): String = txEntity.hash
            override fun amount(): BigDecimal = txEntity.amount
            override fun destination(): String = getDestination(txEntity)
            override fun paymentReference(): String? = txEntity.tag
            override fun fee(): BigDecimal = txEntity.fee
            override fun status(): TxStatus = txEntity.status
            override fun index(): Long = txEntity.index
        }
    }
}