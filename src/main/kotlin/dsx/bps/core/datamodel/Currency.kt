package dsx.bps.core.datamodel

import com.uchuhimo.konf.ConfigSpec
import dsx.bps.config.currencies.BtcConfig
import dsx.bps.config.currencies.TrxConfig
import dsx.bps.config.currencies.XrpConfig

enum class Currency {
    BTC {
        override val coinConfigSpec = BtcConfig
    },
    XRP {
        override val coinConfigSpec = XrpConfig
    },
    TRX {
        override val coinConfigSpec = TrxConfig
    };

    abstract val coinConfigSpec: ConfigSpec
}