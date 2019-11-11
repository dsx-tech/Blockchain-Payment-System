package dsx.bps.crypto.common

import com.uchuhimo.konf.Config
import java.util.*
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.btc.BtcClient
import dsx.bps.crypto.trx.TrxClient
import dsx.bps.crypto.xrp.XrpClient

class CoinClientFactory {

    companion object {
        fun createCoinClient(currency: Currency, config: Config): CoinClient = when (currency) {
            Currency.BTC -> BtcClient(config)
            Currency.XRP -> XrpClient(config)
            Currency.TRX -> TrxClient(config)
        }
    }
}