package dsx.bps.core.datamodel

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

    fun getAddresses(currency: Currency): MutableList<String> { //TODO ex if not in enabled
        return addresses[currency]!!
    }

    fun addNewAddress(address: String, currency: Currency) {
        addresses[currency]!!.add(address) // TODO ex if no cur
    }

    fun addTx(tx: Tx) {
        txids[tx.currency()]!!.add(tx.txid()) // TODO ex if no cur
    }
}