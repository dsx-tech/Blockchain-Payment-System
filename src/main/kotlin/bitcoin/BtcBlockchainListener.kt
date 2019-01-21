package dsx.bps.kotlin.bitcoin

import dsx.bps.kotlin.core.BlockchainListener
import java.util.concurrent.Executors
import kotlin.concurrent.timer

class BtcBlockchainListener(var rpc: BtcRPC): BlockchainListener() {

    var frequency: Long = 5000 // 5 sec

    override val viewedBlocks = hashSetOf<String>()

    override var lastBestHash: String? = null

    override var height: Int = rpc.blockCount
        get() = rpc.blockCount

    init {
        viewedBlocks.add(rpc.bestBlockHash)

        timer("BtcBlockchainListener", true, 0, frequency) {

//            val lastViewedBlock = rpc.getBlock(lastBestHash, 2)
//            if (lastViewedBlock?.confirmations == -1) {
//
//            }

            var hash = rpc.bestBlockHash
            if (hash != lastBestHash) {
                while (!viewedBlocks.contains(hash)) {
                    val block = rpc.getBlock(hash, 2) ?: throw RuntimeException("BtcBlockchainListener: could not get block by hash")
                    setChanged()
                    Executors.newSingleThreadExecutor().execute { notifyObservers(block) }
                    viewedBlocks.add(hash)
                    hash = block.previousblockhash
                }
            }
        }
    }
}