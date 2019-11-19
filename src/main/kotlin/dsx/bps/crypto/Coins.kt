package dsx.bps.crypto

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.EnabledCoinsConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.common.CoinFactory
import java.io.File
import java.math.BigDecimal

class Coins {

    val coins: Map<Currency, Coin>

    constructor(configFile: File) {
        val enabledCoinsConfig = with(Config()) {
            addSpec(EnabledCoinsConfig)
            from.yaml.file(configFile)
        }
        enabledCoinsConfig.validateRequired()

        val mutableCoinsMap: MutableMap<Currency, Coin> = mutableMapOf()

        for (coin in enabledCoinsConfig[EnabledCoinsConfig.coins]) {
            val coinConfig = with(Config()) {
                addSpec(coin.coinConfigSpec)
                from.yaml.file(configFile)
            }
            coinConfig.validateRequired()
            mutableCoinsMap[coin] = CoinFactory.createCoinClient(coin, coinConfig)
        }

        coins = mutableCoinsMap.toMap()
    }

    constructor(coinsMap: Map<Currency, Coin>) {
        coins = coinsMap
    }

    private fun getCoin(currency: Currency): Coin = coins[currency]
        ?: throw Exception("Currency ${currency.name} isn't specified in configuration file.")

    fun getAddress(currency: Currency): String {
        val coin = getCoin(currency)
        return coin.getAddress()
    }

    fun getBalance(currency: Currency): BigDecimal {
        val coin = getCoin(currency)
        return coin.getBalance()
    }

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): Tx {
        val coin = getCoin(currency)
        return coin.sendPayment(amount, address, tag)
    }

    fun getTag(currency: Currency): Int? {
        val coin = getCoin(currency)
        return coin.getTag()
    }

    fun getTx(currency: Currency, txid: TxId): Tx {
        val coin = getCoin(currency)
        return coin.getTx(txid)
    }

    fun getTxs(currency: Currency, txids: List<TxId>): List<Tx> {
        val coin = getCoin(currency)

        return txids
            .mapNotNull { txid ->
                try {
                    coin.getTx(txid)
                } catch (ex: Exception) {
                    println(ex.message)
                    null
                }
            }
    }
}