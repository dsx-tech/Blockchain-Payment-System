package dsx.bps.api

import java.math.BigDecimal
import dsx.bps.core.Currency
import dsx.bps.core.Invoice
import dsx.bps.core.Payment
import dsx.bps.core.BlockchainPaymentSystemManager

class BlockchainPaymentSystemAPI {

    private val manager: BlockchainPaymentSystemManager

    constructor() {
        manager = BlockchainPaymentSystemManager()
    }

    constructor(confPath: String) {
        manager = BlockchainPaymentSystemManager(confPath)
    }

    fun sendPayment(currency: Currency, amount: Number, address: String): String {
        val am = BigDecimal(amount.toString())
        return manager.sendPayment(currency, am, address)
    }

    fun createInvoice(currency: Currency, amount: Number): String {
        val am = BigDecimal(amount.toString())
        return manager.createInvoice(currency, am)
    }

    fun getPayment(id: String): Payment {
        TODO("not implemented")
    }

    fun getInvoice(id: String): Invoice {
        TODO("not implemented")
    }

    fun getBalance(currency: Currency): String {
        val balance = manager.getBalance(currency)
        return balance.toPlainString()
    }

}