package dsx.bps.crypto.btc

import dsx.bps.crypto.common.Explorer
import java.util.*
import kotlin.concurrent.timer

class BtcExplorer(override val coin: BtcCoin, conf: Properties): Explorer(conf) {

    override val currency = coin.currency
    override var frequency = config.getProperty("BTC.frequency", "5000").toLong()

    init {
        explore()
    }

    override fun explore() {
        var last = coin.getBestBlockHash()
        viewed.add(last)

        timer(this::class.toString(), true, 0, frequency) {
            var new = coin.getBestBlockHash()
            if (last != new) {
                last = new
                while (!viewed.contains(new)) {
                    viewed.add(new)
                    val block = coin.getBlock(new)
                    new = block.previousblockhash
                }
                coin.listSinceBlock(new)
                    .transactions
                    .forEach {
                        val tx = coin.constructTx(it)
                        emitter.onNext(tx)
                    }
            }
        }
    }
}