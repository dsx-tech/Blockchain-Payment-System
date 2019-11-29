package dsx.bps.config.currencies

import com.uchuhimo.konf.ConfigSpec
import dsx.bps.core.datamodel.Currency

object EnabledCurrenciesConfig : ConfigSpec("enabledCurrencies") {
    val coins by required<Set<Currency>>()
}