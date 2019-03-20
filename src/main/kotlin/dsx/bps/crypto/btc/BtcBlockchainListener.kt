package dsx.bps.crypto.btc

import dsx.bps.crypto.common.BlockchainListener
import kotlin.concurrent.timer

class BtcBlockchainListener(override val coin: BtcClient, frequency: Long): BlockchainListener(frequency) {

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