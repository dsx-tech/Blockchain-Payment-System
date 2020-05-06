package dsx.bps.core

import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.DepositAccountService
import dsx.bps.DBservices.core.TxService
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.DepositAccount
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.exception.core.depositAccount.DepositAccountException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlin.concurrent.timer

class DepositAccountProcessor(
    private val manager: BlockchainPaymentSystemManager,
    datasource: Datasource, txServ: TxService
): Observer<Tx> {

    private val depositAccountService = DepositAccountService(datasource)
    private val txService = txServ
    private val depositAccounts = depositAccountService.getDepositAccounts()
    private val depositIds = depositAccountService.getDepositIds()
    val frequency: Long = 5000 // TODO frequency from config

    init {
        manager.subscribe(this)
        check()
    }

    private fun check() {
        timer(this::class.toString(), true, 0, frequency) {
            depositIds.mapNotNull { depositAccounts[it] }.forEach { recalculate(it) }
        }
    }

    private fun recalculate(depositAccount: DepositAccount) {
        synchronized(depositAccount) {
            depositAccount.enabledCurrency.forEach {
                manager.getTxs(
                    it,
                    depositAccount.txids[it] ?: throw DepositAccountException("no transactions for currency $it")
                ).forEach { tx ->
                    if (tx.status() == TxStatus.REJECTED) {
                        txService.updateStatus(TxStatus.REJECTED, tx.hash(), tx.index())
                    } else if (tx.status() == TxStatus.CONFIRMED) {
                        txService.updateStatus(TxStatus.CONFIRMED, tx.hash(), tx.index())
                    }
                }
            }
        }
    }

    override fun onNext(tx: Tx) {
        depositIds.mapNotNull { id -> depositAccounts[id] }.filter { dep -> hasAddress(dep, tx) }.forEach { dep ->
            synchronized(dep) {
                depositAccountService.addTx(dep.depositId, tx.txid())
                dep.addTx(tx)
            }
        }
    }

    private fun hasAddress(depositAccount: DepositAccount, tx: Tx): Boolean {
        return depositAccount.getAddresses(tx.currency()).contains(tx.destination())
    }

    fun createNewAccount(id: String, currencies: List<Currency>) {
        depositAccountService.add(id, currencies)
        depositAccounts[id] = DepositAccount(id, currencies)
        depositIds.add(id)
    }

    fun createNewAddress(id: String, currency: Currency, address: String): String {
        if (depositIds.contains(id)) {
            depositAccountService.addNewAddress(address, currency, id)
            depositAccounts[id]!!.addNewAddress(address, currency)
            return address
        }
        else
            throw DepositAccountException("no such id $id")
    }

    fun getAllTx(id: String, currency: Currency): List<Tx> {
        if (!depositIds.contains(id))
            throw DepositAccountException("no such id $id")
        return depositAccountService.getAllTx(id, currency).map { txService.constructTxByTxEntity(it) }
    }

    fun getLastTx(id: String, currency: Currency, amount: Int): List<Tx> {
        if (!depositIds.contains(id))
            throw DepositAccountException("no such id $id")
        return depositAccountService.getLastTx(id, currency, amount).map { txService.constructTxByTxEntity(it) }
    }

    override fun onError(e: Throwable) {
        println(e.message + ":\n" + e.stackTrace)
    }

    override fun onComplete() {}

    override fun onSubscribe(d: Disposable) {}
}