package dsx.bps.core.datamodel

import com.uchuhimo.konf.ConfigSpec
import dsx.bps.config.currencyconfig.BtcConfig
import dsx.bps.config.currencyconfig.TrxConfig
import dsx.bps.config.currencyconfig.XrpConfig

enum class Currency {
    BTC{
        override val coinConfigSpec = BtcConfig
    },
    XRP{
        override val coinConfigSpec = XrpConfig
    },
    TRX{
        override val coinConfigSpec = TrxConfig
    };

    abstract val coinConfigSpec: ConfigSpec
}