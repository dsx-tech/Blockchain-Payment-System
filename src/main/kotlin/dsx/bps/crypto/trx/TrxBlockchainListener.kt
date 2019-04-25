package dsx.bps.crypto.trx

import dsx.bps.crypto.common.BlockchainListener
import kotlin.concurrent.timer

class TrxBlockchainListener(override val coin: TrxClient, frequency: Long): BlockchainListener(frequency) {

    override val currency = coin.currency

    init {
        explore()
    }

    override fun explore() {}
}