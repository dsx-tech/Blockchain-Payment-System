package dsx.bps.kotlin.btc

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

            val newBestHash = rpc.bestBlockHash
            if (newBestHash != lastBestHash) {
                var hash = newBestHash
                while (!viewedBlocks.contains(hash)) {
                    viewedBlocks.add(hash)
                    val block = rpc.getBlock(hash, 2)
                        ?: throw RuntimeException("BtcBlockchainListener: could not get block by hash")
                    hash = block.previousblockhash
                }
                setChanged()
                val listSinceBlock = rpc.listSinceBlock(hash)
                Executors.newSingleThreadExecutor().execute { notifyObservers(listSinceBlock.transactions) }
                lastBestHash = newBestHash
            }
        }
    }
}