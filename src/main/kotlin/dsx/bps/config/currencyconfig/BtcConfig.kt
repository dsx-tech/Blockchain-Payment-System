package dsx.bps.config.currencyconfig

import com.uchuhimo.konf.ConfigSpec

object BtcConfig: ConfigSpec("btcConfig"){
    val user by required<String>()
    val password by required<String>()
    val host by required<String>()
    val port by required<String>()
    val confirmations by required<Int>()
    val frequency by required<Long>()
}
