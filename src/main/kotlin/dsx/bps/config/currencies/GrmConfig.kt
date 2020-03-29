package dsx.bps.config.currencies

import com.uchuhimo.konf.ConfigSpec

object GrmConfig : ConfigSpec("GRM") {
    object Coin : ConfigSpec("coin") {
        val accountAddress by required<String>()
    }

    object Connection : ConfigSpec("connection") {
        val pathToTonClientConfig by required<String>()
        val keyStorePath by required<String>()
        val logVerbosityLevel by required<Int>()
    }

    object Explorer : ConfigSpec("explorer") {
        val frequency by required<Long>()
    }
}