package dsx.bps.crypto.xrp

import dsx.bps.crypto.common.BlockchainListener
import kotlin.concurrent.timer

class XrpBlockchainListener(override val coin: XrpClient, frequency: Long): BlockchainListener(frequency) {

    override val currency = coin.currency

    init {
        explore()
    }

    override fun explore() {
        var lastIndex = coin.getLastLedger().index

        timer(this::class.toString(), true, 0, frequency) {
            val newIndex = coin.getLastLedger().index
            if (lastIndex != newIndex) {
                coin.getAccountTxs(lastIndex+1, newIndex)
                    .transactions
                    .filter {
                        it.tx.type == "Payment" &&
                        it.tx.amount?.currency == currency.name
                    }
                    .forEach {
                        val tx = coin.constructTx(it)
                        emitter.onNext(tx)
                    }
                lastIndex = newIndex
            }
        }
    }
}