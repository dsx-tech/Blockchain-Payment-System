package dsx.bps.crypto.common

import java.util.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.btc.BtcCoin
import dsx.bps.crypto.trx.TrxCoin
import dsx.bps.crypto.xrp.XrpCoin

class CoinFactory {

    companion object {
        fun create(currency: Currency, config: Properties): Coin = when (currency) {
            Currency.BTC -> BtcCoin(config)
            Currency.XRP -> XrpCoin(config)
            Currency.TRX -> TrxCoin(config)
        }
    }
}