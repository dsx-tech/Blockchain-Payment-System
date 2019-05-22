package dsx.bps.crypto.common

import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.btc.BtcCoin
import dsx.bps.crypto.btc.BtcExplorer
import dsx.bps.crypto.trx.TrxCoin
import dsx.bps.crypto.trx.TrxExplorer
import dsx.bps.crypto.xrp.XrpCoin
import dsx.bps.crypto.xrp.XrpExplorer
import java.util.*

class ExplorerFactory {
    companion object {
        fun create(coin: Coin, config: Properties): Explorer = when (coin.currency) {
            Currency.BTC -> BtcExplorer(coin as BtcCoin, config)
            Currency.XRP -> XrpExplorer(coin as XrpCoin, config)
            Currency.TRX -> TrxExplorer(coin as TrxCoin, config)
        }
    }
}