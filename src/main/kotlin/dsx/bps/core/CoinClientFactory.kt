package dsx.bps.core

import dsx.bps.crypto.btc.BtcClient
import java.util.*

class CoinClientFactory {

    companion object {
        fun createCoinClient(currency: Currency, config: Properties): CoinClient = when (currency) {
            Currency.BTC -> BtcClient(config)
        }
    }
}