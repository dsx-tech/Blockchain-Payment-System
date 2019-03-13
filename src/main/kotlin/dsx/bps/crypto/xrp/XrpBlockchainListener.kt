package dsx.bps.crypto.xrp

import dsx.bps.crypto.common.BlockchainListener
import dsx.bps.core.datamodel.Tx
import io.reactivex.subjects.PublishSubject
import kotlin.concurrent.timer

class XrpBlockchainListener(private val rpc: XrpRpc, private val account: String): BlockchainListener {

    override var frequency: Long = 5000

    override val viewed: HashSet<String> = hashSetOf()

    override val emitter: PublishSubject<Tx> = PublishSubject.create()

    init {
        explore()
    }

    override fun explore() {
        val lastLedger = rpc.getLastLedger()
        var lastIndex = lastLedger.index
        var lastHash = lastLedger.hash
        viewed.add(lastHash)

        timer(this::class.toString(), true, 0, frequency) {
            var ledger = rpc.getLastLedger()
            val newIndex = ledger.index
            var newHash = ledger.hash
            if (newHash != lastHash) {
                lastHash = newHash
                while (!viewed.contains(newHash)) {
                    viewed.add(newHash)
                    ledger = rpc.getLedger(ledger.previousHash)
                    newHash = ledger.hash
                }
                val accountTxs = rpc.getAccountTx(account, lastIndex+1, newIndex)
                accountTxs
                    .filter { it.type == "Payment" }
                    .forEach(emitter::onNext)
                lastIndex = newIndex
            }
        }
    }
}