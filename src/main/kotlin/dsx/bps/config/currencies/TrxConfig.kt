package dsx.bps.config.currencies

import com.uchuhimo.konf.ConfigSpec

object TrxConfig: ConfigSpec("trxConfig"){
    val account by required<String>()
    val accountAddress by required<String>()
    val privateKey by required<String>()
    val host by required<String>()
    val port by required<String>()
    val confirmations by required<Int>()
    val frequency by required<Long>()
}
