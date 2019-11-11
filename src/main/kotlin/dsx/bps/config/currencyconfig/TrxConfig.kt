package dsx.bps.config.currencyconfig

import com.uchuhimo.konf.ConfigSpec

class TrxConfig {
    companion object: ConfigSpec("trxConfig"){
        val enabled by required<Boolean>()
        val account by required<String>()
        val accountAddress by required<String>()
        val privateKey by required<String>()
        val host by required<String>()
        val port by required<String>()
        val confirmations by required<Int>()
        val frequency by required<Long>()
    }
}