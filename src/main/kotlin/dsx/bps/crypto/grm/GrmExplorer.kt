package dsx.bps.crypto.grm

import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import dsx.bps.crypto.grm.datamodel.GrmInternalTxId
import kotlin.concurrent.timer

class GrmExplorer(override val coin: GrmCoin, frequency: Long) : Explorer(frequency) {

    override val currency: Currency = coin.currency

    init {
        explore()
    }

    override fun explore() {
        var lastTxId: GrmInternalTxId = coin.getLastInternalTxId()

        timer(this::class.toString(), true, 0, frequency) {
            val newTransactionId = coin.getLastInternalTxId()

            if (lastTxId != newTransactionId) {
                coin.getAccountTxs(lastTxId).filter {
                    it.inMsg.value > 0
                            && it.inMsg.msgData.body.isNotEmpty()
                }.forEach {
                    val tx = coin.constructTx(it)
                    when (tx.tag()) {
                        checkNotNull(tx.tag()) -> emitter.onNext(tx)
                    }
                }
                lastTxId = newTransactionId
            }
        }
    }
}