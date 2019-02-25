package dsx.bps.crypto.btc

import kotlin.concurrent.timer
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import dsx.bps.crypto.common.BlockchainListener

class BtcBlockchainListener(var rpc: BtcRPC): BlockchainListener() {

    var frequency: Long = 5000 // 5 sec

    override val viewedBlocks = hashSetOf<String>()

    override var lastBestHash: String? = null

    override val height: Int
        get() = rpc.blockCount

    override val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        viewedBlocks.add(rpc.bestBlockHash)

        timer("BtcBlockchainListener", true, 0, frequency) {

            val newBestHash = rpc.bestBlockHash
            if (newBestHash != lastBestHash) {
                var hash = newBestHash
                while (!viewedBlocks.contains(hash)) {
                    viewedBlocks.add(hash)
                    val block = rpc.getBlock(hash)
                    hash = block.previousblockhash
                }
                setChanged()
                val listSinceBlock = rpc.listSinceBlock(hash)
                executorService.execute { notifyObservers(listSinceBlock.transactions) }
                lastBestHash = newBestHash
            }
        }
    }
}