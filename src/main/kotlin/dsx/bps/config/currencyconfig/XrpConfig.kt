package dsx.bps.config.currencyconfig

import com.uchuhimo.konf.ConfigSpec

object XrpConfig: ConfigSpec("xrpConfig"){
    val account by required<String>()
    val privateKey by required<String>()
    val passPhrase by required<String>()
    val host by required<String>()
    val port by required<String>()
    val frequency by required<Long>()
}
