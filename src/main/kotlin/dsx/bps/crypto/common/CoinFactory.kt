package dsx.bps.crypto.common

import com.uchuhimo.konf.Config
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.btc.BtcCoin
import dsx.bps.crypto.trx.TrxCoin
import dsx.bps.crypto.xrp.XrpCoin

class CoinFactory {

    companion object {
        fun createCoinClient(currency: Currency, config: Config): Coin = when (currency) {
            Currency.BTC -> BtcCoin(config)
            Currency.XRP -> XrpCoin(config)
            Currency.TRX -> TrxCoin(config)
        }
    }
}