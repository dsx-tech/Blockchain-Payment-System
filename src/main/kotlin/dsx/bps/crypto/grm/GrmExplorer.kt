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
        //TODO: После добавление БД, нужна процедура восстановления после сбоя
        var lastTxId: GrmInternalTxId = coin.getLastInternalTxId()

        timer(this::class.toString(), true, 0, frequency) {
            val newTxId = coin.getLastInternalTxId()
            val notProcesssedGrmTxs = coin.getAccountTxs(newTxId, lastTxId)

            for (grmTx in notProcesssedGrmTxs) {
                //TODO: Разобраться как идентифицировать валидные и невалидные транзакции
                if (grmTx.inMsg.value > 0 && grmTx.inMsg.msgData.body.isNotEmpty()) {
                    val tx = coin.constructTx(grmTx)
                    emitter.onNext(tx)
                }
            }
            lastTxId = newTxId
        }
    }
}