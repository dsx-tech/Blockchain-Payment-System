package dsx.bps.crypto.trx

import dsx.bps.crypto.common.Explorer
import java.util.*
import kotlin.concurrent.timer

class TrxExplorer(override val coin: TrxCoin, conf: Properties): Explorer(conf) {

    override val currency = coin.currency
    override var frequency = config.getProperty("TRX.frequency", "2000").toLong()

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