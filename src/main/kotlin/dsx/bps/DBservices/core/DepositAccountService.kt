package dsx.bps.DBservices.core

import dsx.bps.DBclasses.core.DepositAccountEntity
import dsx.bps.DBclasses.core.DepositAccountTable
import dsx.bps.DBclasses.core.EnabledCurrencyEntity
import dsx.bps.DBclasses.core.EnabledCurrencyTable
import dsx.bps.DBclasses.core.CryptoAddressEntity
import dsx.bps.DBclasses.core.DepositAddressEntity
import dsx.bps.DBclasses.core.DepositAddressTable
import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.core.tx.TxTable
import dsx.bps.DBservices.Datasource
import dsx.bps.core.datamodel.DepositAccount
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.PayableType
import dsx.bps.core.datamodel.TxId
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.max

class DepositAccountService(datasource: Datasource) {
    init {
        transaction(datasource.getConnection()) {
            if (!DepositAccountTable.exists())
                SchemaUtils.create(DepositAccountTable)
            if (!EnabledCurrencyTable.exists())
                SchemaUtils.create(EnabledCurrencyTable)
            if (!DepositAddressTable.exists())
                SchemaUtils.create(DepositAddressTable)
            if (!TxTable.exists())
                SchemaUtils.create(TxTable)
        }
    }

    fun add(depositId: String, enabledCurrency: List<Currency>) {
        transaction {
            val depAcc = DepositAccountEntity.new {
                depositAccountId = depositId
            }

            enabledCurrency.forEach { cur ->
                EnabledCurrencyEntity.new {
                    currency = cur
                    depositAccount = depAcc
                }
            }
        }
    }

    fun addNewAddress(address: String, currency: Currency, depositId: String) {
        transaction {
            val crAdd = CryptoAddressEntity.new {
                this.address = address
                this.currency = currency
                this.type = PayableType.DepositAccount
            }
            DepositAddressEntity.new {
                this.cryptoAddress = crAdd
                this.depositAccount = getByDepositId(depositId)
            }
        }
    }

    fun getByDepositId(depositId: String): DepositAccountEntity {
        return transaction { DepositAccountEntity.find { DepositAccountTable.depositAccountId eq depositId }.first() }
    }

    fun getDepositIds(): MutableList<String> {
        val depositIds = mutableListOf<String>()
        transaction {
            DepositAccountEntity.all().forEach { depositIds.add(it.depositAccountId) }
        }
        return depositIds
    }

    fun getDepositAccounts(): MutableMap<String, DepositAccount> {
        val depositAccountMap = mutableMapOf<String, DepositAccount>()
        transaction {
            DepositAccountEntity.all().forEach {
                depositAccountMap[it.depositAccountId] = makeDepositAccountFromDB(it)
            }
        }
        return depositAccountMap
    }

    fun makeDepositAccountFromDB(depositAccount: DepositAccountEntity): DepositAccount {
        return transaction {
            val depAcc = DepositAccount(
                depositAccount.depositAccountId,
                depositAccount.enabledCurrency.toList().map { it.currency })
            depositAccount.depositAddress.map { it.cryptoAddress }.forEach{ crAdd ->
                depAcc.addresses[crAdd.currency]!!.add(crAdd.address)
                crAdd.txs.forEach { txEntity ->
                    depAcc.txids[crAdd.currency]!!.add(TxId(txEntity.hash, txEntity.index))
                }
            }
            return@transaction depAcc
        }
    }

    fun getAllTx(depositId: String, currency: Currency): List<TxEntity> {
        val txList = mutableListOf<TxEntity>()
        transaction {
            getByDepositId(depositId).depositAddress.map { it.cryptoAddress }.filter { it.currency == currency }.forEach { crAdd ->
                txList.addAll(crAdd.txs.toList())
            }
        }
        return txList
    }

    fun getLastTxToAddress(depositId: String, currency: Currency, address: String, amount: Int): List<TxEntity> {
        return transaction {
            val txList = getByDepositId(depositId).depositAddress.map { it.cryptoAddress }.filter { it.currency == currency && it.address == address }.first().txs.sortedBy { it.id }
            return@transaction txList.subList(max(txList.size - amount, 0), txList.size)
        }
    }
}