package dsx.bps.core.datamodel

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import dsx.bps.config.currencies.BtcConfig
import dsx.bps.config.currencies.TrxConfig
import dsx.bps.config.currencies.XrpConfig
import dsx.bps.crypto.btc.BtcCoin
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.trx.TrxCoin
import dsx.bps.crypto.xrp.XrpCoin

enum class Currency {
    BTC{
        override val coinConfigSpec = BtcConfig
        override fun createCoin(config: Config): BtcCoin = BtcCoin(config)
    },
    XRP{
        override val coinConfigSpec = XrpConfig
        override fun createCoin(config: Config): XrpCoin = XrpCoin(config)
    },
    TRX{
        override val coinConfigSpec = TrxConfig
        override fun createCoin(config: Config): TrxCoin = TrxCoin(config)
    };

    abstract val coinConfigSpec: ConfigSpec
    abstract fun createCoin(config: Config): Coin
}