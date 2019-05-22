package dsx.bps.crypto

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.common.CoinFactory
import java.math.BigDecimal
import java.util.*

class Coins(private val config: Properties) {

    val coins: Map<Currency, Coin>

    init {
        coins = Currency
            .values()
            .filter { config.getProperty(it.name) == "1" }
            .mapNotNull {
                try {
                    it to CoinFactory.create(it, config)
                } catch (ex: Exception) {
                    // TODO: replace with logging
                    println("Failed to create client for ${it.name}:\n" + ex.message)
                    null
                }
            }.toMap()
    }

    private fun getCoin(currency: Currency): Coin = coins[currency]
        ?: throw Exception("Currency ${currency.name} isn't specified in configuration file.")

    fun address(currency: Currency): String {
        val coin = getCoin(currency)
        return coin.address
    }

    fun balance(currency: Currency): BigDecimal {
        val coin = getCoin(currency)
        return coin.balance
    }

    fun send(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): Tx {
        val coin = getCoin(currency)
        return coin.send(amount, address, tag)
    }

    fun tag(currency: Currency): Int? {
        val coin = getCoin(currency)
        return coin.tag()
    }

    fun tx(currency: Currency, txid: TxId): Tx {
        val coin = getCoin(currency)
        return coin.tx(txid)
    }

    fun txs(currency: Currency, txids: List<TxId>): List<Tx> {
        val coin = getCoin(currency)

        return txids
            .mapNotNull { txid ->
                try {
                    coin.tx(txid)
                } catch (ex: Exception) {
                    println(ex.message)
                    null
                }
            }
    }
}