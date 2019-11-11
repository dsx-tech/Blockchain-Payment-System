package dsx.bps.config.currencyconfig

import com.uchuhimo.konf.ConfigSpec

class BtcConfig {
    companion object: ConfigSpec("btcConfig"){
        val enabled by required<Boolean>()
        val user by required<String>()
        val password by required<String>()
        val host by required<String>()
        val port by required<String>()
        val confirmations by required<Int>()
        val frequency by required<Long>()
    }
}