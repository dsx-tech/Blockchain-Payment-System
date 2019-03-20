package dsx.bps.api

import java.math.BigDecimal
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Invoice
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.BlockchainPaymentSystemManager

class BlockchainPaymentSystemAPI {

    private val manager: BlockchainPaymentSystemManager

    constructor() {
        manager = BlockchainPaymentSystemManager()
    }

    constructor(confPath: String) {
        manager = BlockchainPaymentSystemManager(confPath)
    }

    fun sendPayment(currency: Currency, amount: Number, address: String, tag: Int? = null): String {
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

}