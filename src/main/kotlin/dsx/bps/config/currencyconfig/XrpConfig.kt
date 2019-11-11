package dsx.bps.config.currencyconfig

import com.uchuhimo.konf.ConfigSpec

class XrpConfig {
    companion object: ConfigSpec(){
        val enabled by required<Boolean>()
        val account by required<String>()
        val privateKey by required<String>()
        val passPhrase by required<String>()
        val host by required<String>()
        val port by required<String>()
        val frequency by required<Long>()
    }
}