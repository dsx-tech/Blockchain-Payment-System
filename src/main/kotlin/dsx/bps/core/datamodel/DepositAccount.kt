package dsx.bps.core.datamodel

import dsx.bps.exception.core.depositAccount.DepositAccountException
import java.util.Collections

class DepositAccount(val depositId: String, currencies: List<Currency>) {
    val enabledCurrency = currencies
    val addresses = mutableMapOf<Currency, MutableList<String>>()
    val txids = Collections.synchronizedMap(mutableMapOf<Currency, MutableList<TxId>>())

    init {
        enabledCurrency.forEach {
            addresses[it] = mutableListOf()
            txids[it] = mutableListOf()
        }
    }

    fun getAddresses(currency: Currency): MutableList<String> {
        if (!enabledCurrency.contains(currency))
            throw DepositAccountException("this currency is not enabled $currency")
        return addresses[currency]!!
    }

    fun addNewAddress(address: String, currency: Currency) {
        if (!enabledCurrency.contains(currency))
            throw DepositAccountException("this currency is not enabled $currency")
        addresses[currency]!!.add(address)
    }

    fun addTx(tx: Tx) {
        if (!enabledCurrency.contains(tx.currency()))
            throw DepositAccountException("this currency is not enabled ${tx.currency()}")
        txids[tx.currency()]!!.add(tx.txid())
    }
}