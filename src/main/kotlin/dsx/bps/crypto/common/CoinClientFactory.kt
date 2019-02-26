package dsx.bps.crypto.common

import java.util.*
import dsx.bps.core.Currency
import dsx.bps.crypto.btc.BtcClient

class CoinClientFactory {

    companion object {
        fun createCoinClient(currency: Currency, config: Properties): CoinClient = when (currency) {
            Currency.BTC -> BtcClient(config)
        }
    }
}