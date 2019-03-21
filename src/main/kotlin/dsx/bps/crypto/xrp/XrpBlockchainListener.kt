package dsx.bps.crypto.xrp

import dsx.bps.crypto.common.BlockchainListener
import kotlin.concurrent.timer

class XrpBlockchainListener(override val coin: XrpClient, frequency: Long): BlockchainListener(frequency) {

    override val currency = coin.currency

    init {
        explore()
    }

    override fun explore() {
        val lastLedger = coin.getLastLedger()
        var lastIndex = lastLedger.index
        var lastHash = lastLedger.hash
        viewed.add(lastHash)

        timer(this::class.toString(), true, 0, frequency) {
            var ledger = coin.getLastLedger()
            val newIndex = ledger.index
            var newHash = ledger.hash
            if (lastHash != newHash) {
                lastHash = newHash
                while (!viewed.contains(newHash)) {
                    viewed.add(newHash)
                    ledger = coin.getLedger(ledger.previousHash)
                    newHash = ledger.hash
                }
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