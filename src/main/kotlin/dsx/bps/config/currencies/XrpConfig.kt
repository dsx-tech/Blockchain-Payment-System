package dsx.bps.config.currencies

import com.uchuhimo.konf.ConfigSpec

object XrpConfig: ConfigSpec("XRP") {
    object Coin: ConfigSpec("coin") {
        val account by required<String>()
        val privateKey by required<String>()
        val passPhrase by required<String>()
    }
    object Connection: ConfigSpec("connection") {
        val host by required<String>()
        val port by required<String>()
    }
    object Explorer: ConfigSpec("explorer") {
        val frequency by required<Long>()
    }
}
