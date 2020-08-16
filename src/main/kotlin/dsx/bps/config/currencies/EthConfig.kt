package dsx.bps.config.currencies

import com.uchuhimo.konf.ConfigSpec

object EthConfig: ConfigSpec("ETH") {
    object Coin: ConfigSpec("coin") {
        val accountAddress by required<String>()
        val pathToWallet by required<String>()
        val password by required<String>()
        val defaultPasswordForNewAddresses by required<String>()
        val walletsDir by required<String>()
        val confirmations by required<Int>()
        val scanningCount by required<Int>()
    }

    object Erc20: ConfigSpec("erc20") {
        val tokens by required<Map<String, String>>()
    }

    object Connection: ConfigSpec("connection") {
        val host by required<String>()
        val port by required<String>()
    }

    object Explorer: ConfigSpec("explorer") {
        val frequency by required<Long>()
    }
}