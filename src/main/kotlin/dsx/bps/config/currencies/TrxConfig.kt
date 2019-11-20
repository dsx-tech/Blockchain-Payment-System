package dsx.bps.config.currencies

import com.uchuhimo.konf.ConfigSpec

object TrxConfig: ConfigSpec("trxConfig") {
    object Coin: ConfigSpec("coin") {
        val account by required<String>()
        val accountAddress by required<String>()
        val privateKey by required<String>()
        val confirmations by required<Int>()
    }
    object Connection: ConfigSpec("connection") {
        val host by required<String>()
        val port by required<String>()
    }
    object Explorer: ConfigSpec("explorer") {
        val frequency by required<Long>()
    }
}
