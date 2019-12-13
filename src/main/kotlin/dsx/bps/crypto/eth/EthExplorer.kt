package dsx.bps.crypto.eth

import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import org.web3j.protocol.core.methods.response.Transaction
import kotlin.concurrent.timer

class EthExplorer(override val coin: EthCoin, frequency: Long): Explorer(frequency) {

    override val currency: Currency = coin.currency

    init {
        explore()
    }

    override fun explore() {
        var last = coin.getLatestBlock()
        viewed.add(last.hash)

        timer(this::class.toString(), true, 0, frequency) {
            var new = coin.getLatestBlock()
            if (last.hash != new.hash) {
                val lastCandidate = new
                while (!viewed.contains(new.hash)) {
                    new.transactions
                        .forEach {
                            val tx = coin.constructTx(it.get() as Transaction)
                            emitter.onNext(tx)
                        }
                    viewed.add(new.hash)
                    new = coin.getBlockByHash(new.parentHash)
                }
                last = lastCandidate
            }
        }
    }

}