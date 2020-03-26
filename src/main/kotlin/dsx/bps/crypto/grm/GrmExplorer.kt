package dsx.bps.crypto.grm

import dsx.bps.DBclasses.TxEntity
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.GrmService
import dsx.bps.DBservices.TxService
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import dsx.bps.crypto.grm.datamodel.GrmInternalTxId
import dsx.bps.crypto.grm.datamodel.GrmRawTransaction
import java.math.BigDecimal
import kotlin.concurrent.timer

class GrmExplorer(
    override val coin: GrmCoin, datasource: Datasource,
    txServ: TxService, frequency: Long
) : Explorer(frequency) {

    override val currency: Currency = coin.currency

    private val grmService = GrmService(datasource)
    private val txService = txServ

    init {
        explore()
    }

    override fun explore() {
        var lastTxId: GrmInternalTxId = if (!grmService.tableIsEmpty()) {
            val grmNewestKnownTx: TxEntity = grmService.getGrmNewestKnownTx()
            GrmInternalTxId(grmNewestKnownTx.index, grmNewestKnownTx.hash)
        } else {
            coin.getLastInternalTxId()
        }

        timer(this::class.toString(), true, 0, frequency) {
            val newTxId = coin.getLastInternalTxId()
            val notProcessedGrmTxs = coin.getAccountTxs(newTxId, lastTxId)

            for (grmTx in notProcessedGrmTxs.reversedArray()) {
                processNewGrmTx(grmTx)
            }
            lastTxId = newTxId
        }
    }

    private fun processNewGrmTx(grmTx: GrmRawTransaction) {
        val tx = coin.constructDepositTx(grmTx)
        if (tx.amount() > BigDecimal.ZERO &&
            (tx.paymentReference()?.length ?: -1) < 500
        ) {
            val newTx = txService.add(
                tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                tx.fee(), tx.hash(), tx.index(), tx.currency()
            )
            grmService.add(grmTx.utime, grmTx.storageFee, grmTx.transactionId.lt, newTx)
            emitter.onNext(tx)
        }
    }
}