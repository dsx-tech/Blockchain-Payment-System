package dsx.bps.config.currencyconfig

import com.uchuhimo.konf.ConfigSpec
import dsx.bps.core.datamodel.Currency

object EnabledCoinsConfig : ConfigSpec("enabledCoins") {
    val coins by required<Set<Currency>>()
}