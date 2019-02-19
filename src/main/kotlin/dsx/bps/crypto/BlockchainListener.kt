package dsx.bps.crypto

import java.util.Observable
import kotlin.collections.HashSet

abstract class BlockchainListener: Observable() {

    abstract var height: Int
    abstract var lastBestHash: String?
    protected abstract val viewedBlocks: HashSet<String>

}