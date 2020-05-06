package dsx.bps.DBservices.core

import dsx.bps.DBclasses.core.AddressEntity
import dsx.bps.DBclasses.core.DepositAccountEntity
import dsx.bps.DBclasses.core.DepositAccountTable
import dsx.bps.DBclasses.core.EnabledCurrencyEntity
import dsx.bps.DBclasses.core.PayableEntity
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
            if (!TxTable.exists())
                SchemaUtils.create(TxTable)
        }
    }

    fun add(depositId: String, enabledCurrency: List<Currency>) {
        transaction {
            val depAcc = DepositAccountEntity.new {
                depositAccountId = depositId
                payable = PayableEntity.new { type = PayableType.DepositAccount }
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
            AddressEntity.new {
                this.currency = currency
                this.address = address
                depositAccount = getByDepositId(depositId)
            }
        }
    }

    fun getByDepositId(depositId: String): DepositAccountEntity {
        return transaction { DepositAccountEntity.find { DepositAccountTable.depositAccountId eq depositId }.first() }
    }

    fun addTx(depositId: String, tx: TxId) {
        transaction {
            TxEntity.find { TxTable.hash eq tx.hash and (TxTable.index eq tx.index) }.forEach {
                it.payable = getByDepositId(depositId).payable
            }
        }
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
            val depAcc = DepositAccount(depositAccount.depositAccountId, depositAccount.enabledCurrency.toList().map { it.currency })
            depositAccount.address.forEach { depAcc.addresses[it.currency]!!.add(it.address) } // TODO ex if no cur
            depositAccount.payable.txs.forEach { depAcc.txids[it.currency]!!.add(TxId(it.hash, it.index)) } //TODO ex if no cur

            return@transaction depAcc
        }
    }

    fun getAllTx(depositId: String, currency: Currency): List<TxEntity> {
        return transaction {
            getByDepositId(depositId).payable.txs.filter { it.currency == currency }.toList()
        }
    }

    fun getLastTx(depositId: String, currency: Currency, amount: Int): List<TxEntity> {
        return transaction {
            val txList = getByDepositId(depositId).payable.txs.filter { it.currency == currency }.sortedBy { it.id }
            return@transaction txList.subList(max(txList.size - amount, 0), txList.size)
        }
    }
}