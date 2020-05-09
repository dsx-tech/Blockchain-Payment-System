package dsx.bps.api

import dsx.bps.core.BlockchainPaymentSystemManager
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Invoice
import dsx.bps.core.datamodel.Payment
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
}