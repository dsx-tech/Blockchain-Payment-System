package dsx.bps.crypto.btc

import kotlin.concurrent.timer
import io.reactivex.subjects.PublishSubject
import dsx.bps.crypto.common.Tx
import dsx.bps.crypto.common.BlockchainListener

class BtcBlockchainListener(private val rpc: BtcRPC): BlockchainListener {

    override var frequency: Long = 5000

    override val viewed: HashSet<String> = hashSetOf()

    override val emitter: PublishSubject<Tx> = PublishSubject.create()

    init {
        explore()
    }

    override fun explore() {
        var last = rpc.bestBlockHash
        viewed.add(last)

        timer(this::class.qualifiedName, true, 0, frequency) {
            var new = rpc.bestBlockHash
            if (new != last) {
                last = new
                while (!viewed.contains(new)) {
                    viewed.add(new)
                    val block = rpc.getBlock(new)
                    new = block.previousblockhash
                }
                val listSinceBlock = rpc.listSinceBlock(new)
                listSinceBlock.transactions.forEach(emitter::onNext)
            }
        }
    }
}