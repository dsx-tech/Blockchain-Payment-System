package dsx.bps.crypto

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.EnabledCurrenciesConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.btc.BtcCoin
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.eth.EthCoin
import dsx.bps.crypto.trx.TrxCoin
import dsx.bps.crypto.xrp.XrpCoin
import dsx.bps.exception.rpc.BpsRpcException
import io.reactivex.Observable
import java.io.File
import java.math.BigDecimal

class CoinsManager {

    private val enabledCoins: Map<Currency, Coin>

    constructor(configFile: File) {
        val enabledCurrenciesConfig = with(Config()) {
            addSpec(EnabledCurrenciesConfig)
            from.yaml.file(configFile)
        }
        enabledCurrenciesConfig.validateRequired()

        val mutableCoinsMap: MutableMap<Currency, Coin> = mutableMapOf()
        for (enabledCurrency in enabledCurrenciesConfig[EnabledCurrenciesConfig.coins]) {
            val coinConfig = with(Config()) {
                addSpec(enabledCurrency.coinConfigSpec)
                from.yaml.file(configFile)
            }
            coinConfig.validateRequired()
            mutableCoinsMap[enabledCurrency] = when (enabledCurrency) {
                    Currency.BTC -> BtcCoin(coinConfig)
                    Currency.TRX -> TrxCoin(coinConfig)
                    Currency.XRP -> XrpCoin(coinConfig)
                    Currency.ETH -> EthCoin(coinConfig)
            }
        }
        enabledCoins = mutableCoinsMap.toMap()
    }

    constructor(coinsMap: Map<Currency, Coin>) {
        enabledCoins = coinsMap
    }

    private fun getCoin(currency: Currency): Coin = enabledCoins[currency]
        ?: throw Exception("Currency ${currency.name} isn't specified in configuration file.")

    fun getAddress(currency: Currency): String {
        return getCoin(currency).getAddress()
    }

    fun getBalance(currency: Currency): BigDecimal {
        return getCoin(currency).getBalance()
    }

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String, tag: Int? = null): Tx {
        return getCoin(currency).sendPayment(amount, address, tag)
    }

    fun getTag(currency: Currency): Int? {
        return getCoin(currency).getTag()
    }

    fun getTx(currency: Currency, txid: TxId): Tx {
        return getCoin(currency).getTx(txid)
    }

    fun getTxs(currency: Currency, txids: List<TxId>): List<Tx> {
        return txids
            .mapNotNull { txid ->
                try {
                    getCoin(currency).getTx(txid)
                } catch (ex: BpsRpcException) {
                    println(ex.message)
                    null
                }
            }
    }

    fun getAllEmitters(): Observable<Tx> {
        return Observable.merge(enabledCoins.values.map { it.getTxEmitter() })
    }
}