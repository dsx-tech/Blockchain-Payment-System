package dsx.bps.core

import com.uchuhimo.konf.Config
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.DepositAccountService
import dsx.bps.DBservices.core.TxService
import dsx.bps.config.DepositAccountProcessorConfig
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
    config: Config, datasource: Datasource, txServ: TxService
): Observer<Tx> {

    private val depositAccountService = DepositAccountService(datasource)
    private val txService = txServ
    private val depositAccounts = depositAccountService.getDepositAccounts()
    private val depositIds = depositAccountService.getDepositIds()
    var frequency: Long = config[DepositAccountProcessorConfig.frequency]

    init {
        manager.subscribe(this)
        check()
    }

    fun getDepositAccount(id: String): DepositAccount? = depositAccounts[id]

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
            recalculate(dep)
            synchronized(dep) {
                dep.addTx(tx)
            }
        }
    }

    private fun hasAddress(depositAccount: DepositAccount, tx: Tx): Boolean {
        if (!depositAccount.enabledCurrency.contains(tx.currency()))
            return false
        else
            return depositAccount.getAddresses(tx.currency()).contains(tx.destination())
    }

    fun createNewAccount(id: String, currencies: List<Currency>): DepositAccount {
        depositAccountService.add(id, currencies)
        val depAcc = DepositAccount(id, currencies)
        depositAccounts[id] = depAcc
        depositIds.add(id)
        return depAcc
    }

    fun createNewAddress(id: String, currency: Currency, address: String): String {
        if (depositIds.contains(id)) {
            depositAccountService.addNewAddress(address, currency, id)
            depositAccounts[id]!!.addNewAddress(address, currency)
            return address
        } else
            throw DepositAccountException("no such id $id")
    }

    fun getAllTx(id: String, currency: Currency): List<Tx> {
        if (!depositIds.contains(id))
            throw DepositAccountException("no such id $id")
        return depositAccountService.getAllTx(id, currency).map { txService.constructTxByTxEntity(it) }
    }

    fun getLastTxToAddress(id: String, currency: Currency, address: String, amount: Int): List<Tx> {
        if (!depositIds.contains(id))
            throw DepositAccountException("no such id $id")
        return depositAccountService.getLastTxToAddress(id, currency, address, amount).map { txService.constructTxByTxEntity(it) }
    }

    override fun onError(e: Throwable) {
        println(e.message + ":\n" + e.stackTrace)
    }

    override fun onComplete() {}

    override fun onSubscribe(d: Disposable) {}
}