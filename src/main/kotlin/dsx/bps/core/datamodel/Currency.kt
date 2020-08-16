package dsx.bps.core.datamodel

import com.uchuhimo.konf.ConfigSpec
import dsx.bps.config.currencies.*

enum class Currency {
    BTC {
        override val coinConfigSpec = BtcConfig
    },
    XRP {
        override val coinConfigSpec = XrpConfig
    },
    TRX {
        override val coinConfigSpec = TrxConfig
    },

    ETH {
        override val coinConfigSpec = EthConfig
    },
    GRM {
        override val coinConfigSpec = GrmConfig
    },
    USDT {
        override val coinConfigSpec = EthConfig
    },
    BNB {
        override val coinConfigSpec = EthConfig
    };

    abstract val coinConfigSpec: ConfigSpec
}