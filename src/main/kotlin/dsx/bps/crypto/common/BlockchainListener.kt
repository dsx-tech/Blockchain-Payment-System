package dsx.bps.crypto.common

import java.util.Observable
import java.util.concurrent.ExecutorService
import kotlin.collections.HashSet

abstract class BlockchainListener: Observable() {

    abstract val height: Int
    abstract var lastBestHash: String?
    protected abstract val viewedBlocks: HashSet<String>
    protected abstract val executorService: ExecutorService

}