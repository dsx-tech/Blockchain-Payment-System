package dsx.bps.api

import dsx.bps.core.BlockchainPaymentSystemManager
import dsx.bps.core.datamodel.Currency
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

    fun sendPayment(currency: Currency, amount: Number, address: String, tag: String? = null): String {
        val am = BigDecimal(amount.toString())
        return manager.sendPayment(currency, am, address, tag)
    }

    fun createInvoice(currency: Currency, amount: Number): String {
        val am = BigDecimal(amount.toString())
        return manager.createInvoice(currency, am)
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

    fun getAllTx(id: String, currency: Currency): List<Tx> {
        return manager.getAllTx(id, currency)
    }

    fun getLastTx(id: String, currency: Currency, amount: Int): List<Tx> {
        return manager.getLastTx(id, currency, amount)
    }

}