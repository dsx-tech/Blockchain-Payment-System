package dsx.bps.api

import dsx.bps.core.BlockchainPaymentSystemManager
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.DepositAccount
import dsx.bps.core.datamodel.Invoice
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.Tx
import java.math.BigDecimal

class BlockchainPaymentSystemAPI {

    private val manager: BlockchainPaymentSystemManager

    constructor() {
        manager = BlockchainPaymentSystemManager()
    }

    constructor(confPath: String) {
        manager = BlockchainPaymentSystemManager(confPath)
    }

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String, tag: String? = null): String {
        return manager.sendPayment(currency, amount, address, tag)
    }

    fun createInvoice(currency: Currency, amount: BigDecimal): String {
        return manager.createInvoice(currency, amount)
    }

    fun getPayment(id: String): Payment? = manager.getPayment(id)

    fun getInvoice(id: String): Invoice? = manager.getInvoice(id)

    fun getBalance(currency: Currency): String {
        val balance = manager.getBalance(currency)
        return balance.toPlainString()
    }

    fun createNewAccount(id: String, currencies: List<Currency>) {
        manager.createNewAccount(id, currencies)
    }

    fun createNewAddress(id: String, currency: Currency): String {
        return manager.createNewAddress(id, currency)
    }

    fun getDepositAccount(id: String): DepositAccount? = manager.getDepositAccount(id)

    fun getAllTx(id: String, currency: Currency): List<Tx> {
        return manager.getAllTx(id, currency)
    }

    fun getLastTxToAddress(id: String, currency: Currency, address: String, amount: Int): List<Tx> {
        return manager.getLastTxToAddress(id, currency, address, amount)
    }

    @Deprecated("only for tests")
    fun kill(currency: Currency){
        manager.kill(currency)
    }

    @Deprecated("only for tests")
    fun clearDb(currency: Currency){
        manager.clearDb(currency)
    }
}