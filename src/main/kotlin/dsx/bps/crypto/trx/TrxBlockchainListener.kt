package dsx.bps.crypto.trx

import dsx.bps.crypto.common.BlockchainListener
import kotlin.concurrent.timer

class TrxBlockchainListener(override val coin: TrxClient, frequency: Long): BlockchainListener(frequency) {

    override val currency = coin.currency

    init {
        explore()
    }

    override fun explore() {
        var last = coin.getNowBlock()
        viewed.add(last.hash)

        timer(this::class.toString(), true, 0, frequency) {
            var new = coin.getNowBlock()
            if (last.hash != new.hash) {
                last = new
                while (!viewed.contains(new.hash)) {
                    new.transactions
                        .forEach {
                            val tx = coin.constructTx(it)
                            emitter.onNext(tx)
                        }
                    viewed.add(new.hash)
                    new = coin.getBlockById(new.blockHeader.rawData.parentHash)
                }
            }
        }

    }
}