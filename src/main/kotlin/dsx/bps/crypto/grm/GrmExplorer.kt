package dsx.bps.crypto.grm

import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.crypto.grm.GrmConstant.inMsgTextMaxLength
import dsx.bps.DBclasses.crypto.grm.GrmConstant.msgTextOversizeValue
import dsx.bps.DBclasses.crypto.grm.GrmConstant.outMsgTextMaxLength
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.grm.GrmInMsgService
import dsx.bps.DBservices.crypto.grm.GrmOutMsgService
import dsx.bps.DBservices.crypto.grm.GrmTxService
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.common.Explorer
import dsx.bps.crypto.grm.datamodel.GrmInternalTxId
import dsx.bps.crypto.grm.datamodel.GrmRawTransaction
import java.math.BigDecimal
import kotlin.concurrent.timer

class GrmExplorer(
        override val coin: GrmCoin,
        private val grmInMsgService: GrmInMsgService,
        private val grmOutMsgService: GrmOutMsgService,
        private val grmTxService: GrmTxService,
        private val txService: TxService,
        frequency: Long
) : Explorer(frequency) {

    override val currency: Currency = coin.currency
    var syncUtime: Long

    init {
        syncUtime = grmTxService.getGrmNewestKnownTx()?.utime
                ?: coin.getFullAccountState().syncUtime
        explore()
    }

    override fun explore() {
        var lastTxId: GrmInternalTxId = if (!grmTxService.tableIsEmpty()) {
            val grmNewestKnownTx: TxEntity = grmTxService.getGrmNewestKnownTx()!!.tx
            GrmInternalTxId(grmNewestKnownTx.index, grmNewestKnownTx.hash)
        } else {
            coin.getLastInternalTxId()
        }

        timer(this::class.toString(), true, 0, frequency) {
            val accountSyncUtime = coin.getFullAccountState().syncUtime

            val newTxId = coin.getLastInternalTxId()
            val notProcessedGrmTxs = coin.getAccountTxs(coin.accountAddress, newTxId, lastTxId)

            for (i in notProcessedGrmTxs.size - 1 downTo 0) {
                processNewGrmTx(notProcessedGrmTxs[i])
            }
            lastTxId = newTxId

            syncUtime = if (notProcessedGrmTxs.isNotEmpty()) {
                //newest processed tx
                notProcessedGrmTxs[0].utime
            } else {
                accountSyncUtime
            }
        }
    }

    private fun processNewGrmTx(grmTx: GrmRawTransaction) {
        if (grmTx.inMsg.source.isEmpty()) {
            // if tx process external message - save tx in db
            if (grmTx.outMsg.isEmpty()) {
                // if no outMsg in grmTx
                //save tx
                val tx = coin.constructPaymentTx(
                        grmTx.transactionId.hash, grmTx.transactionId.lt, BigDecimal.ZERO,
                        "", "", BigDecimal.ZERO, TxStatus.INCORRECT
                )
                val newTx = txService.add(
                        tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                        tx.fee(), tx.txid().hash, tx.txid().index, tx.currency()
                )
                //save inMsg
                val grmInMsg = grmTx.inMsg
                val inMsg = grmInMsgService.add(
                        grmInMsg.source, grmInMsg.destination, grmInMsg.value,
                        grmInMsg.fwdFee, grmInMsg.ihrFee, grmInMsg.createdLt,
                        grmInMsg.bodyHash,
                        if (grmTx.inMsg.msgText.length <= inMsgTextMaxLength)
                            grmTx.inMsg.msgText else msgTextOversizeValue
                )
                //save grmTx
                grmTxService.add(grmTx.utime, inMsg, newTx)
            } else if (grmTx.outMsg.size == 1) {
                // if grmTx has one outMsg
                val grmInMsg = grmTx.inMsg
                val grmOutMsg = grmTx.outMsg.single()
                //save inMsg
                val inMsgEntity = grmInMsgService.add(
                        grmInMsg.source, grmInMsg.destination, grmInMsg.value,
                        grmInMsg.fwdFee, grmInMsg.ihrFee, grmInMsg.createdLt,
                        grmInMsg.bodyHash,
                        if (grmTx.inMsg.msgText.length <= inMsgTextMaxLength)
                            grmTx.inMsg.msgText else msgTextOversizeValue
                )
                //save Tx
                val tx = coin.constructPaymentTx(
                        grmTx.transactionId.hash, grmTx.transactionId.lt,
                        BigDecimal(grmOutMsg.value), grmOutMsg.destination,
                        if (grmOutMsg.msgText.length <= outMsgTextMaxLength)
                            grmOutMsg.msgText else msgTextOversizeValue,
                        BigDecimal(grmOutMsg.fwdFee + grmOutMsg.ihrFee), TxStatus.CONFIRMED
                )
                val txEntity = txService.add(
                        tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                        tx.fee(), tx.txid().hash, tx.txid().index, tx.currency()
                )
                //save grmTx
                val grmEntity = grmTxService.add(grmTx.utime, inMsgEntity, txEntity)
                //save outMsg
                grmOutMsgService.add(
                        grmOutMsg.source, grmOutMsg.destination, grmOutMsg.value,
                        grmOutMsg.fwdFee, grmOutMsg.ihrFee, grmOutMsg.createdLt,
                        grmOutMsg.bodyHash,
                        if (grmOutMsg.msgText.length <= outMsgTextMaxLength)
                            grmOutMsg.msgText else msgTextOversizeValue,
                        grmEntity
                )
            } else if (grmTx.outMsg.size > 1) {
                // if grmTx has many outMsg
                val inMsg = grmTx.inMsg
                //save inMsg
                val inMsgEntity = grmInMsgService.add(
                        inMsg.source, inMsg.destination, inMsg.value,
                        inMsg.fwdFee, inMsg.ihrFee, inMsg.createdLt,
                        inMsg.bodyHash,
                        if (grmTx.inMsg.msgText.length <= inMsgTextMaxLength)
                            grmTx.inMsg.msgText else msgTextOversizeValue
                )
                //save Tx
                val tx = coin.constructPaymentTx(
                        grmTx.transactionId.hash, grmTx.transactionId.lt,
                        BigDecimal.ZERO, "", "",
                        BigDecimal.ZERO, TxStatus.INCORRECT
                )
                val txEntity = txService.add(
                        tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                        tx.fee(), tx.txid().hash, tx.txid().index, tx.currency()
                )
                //save grmTx
                val grmEntity = grmTxService.add(grmTx.utime, inMsgEntity, txEntity)
                //save outMsgs
                for (outMsg in grmTx.outMsg) {
                    grmOutMsgService.add(
                            outMsg.source, outMsg.destination, outMsg.value,
                            outMsg.fwdFee, outMsg.ihrFee, outMsg.createdLt,
                            outMsg.bodyHash,
                            if (outMsg.msgText.length <= outMsgTextMaxLength)
                                outMsg.msgText else msgTextOversizeValue,
                            grmEntity
                    )
                }
            }
        } else {
            // if tx process internal message - save tx in db and emit it
            //save tx
            val tx = coin.constructDepositTx(grmTx)
            if (txService.checkCryptoAddress(tx)) {
                val txEntity = txService.add(
                    tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                    tx.fee(), tx.txid().hash, tx.txid().index, tx.currency()
                )
                val grmInMsg = grmTx.inMsg
                //save inMsg
                val inMsgEntity = grmInMsgService.add(
                    grmInMsg.source, grmInMsg.destination, grmInMsg.value,
                    grmInMsg.fwdFee, grmInMsg.ihrFee, grmInMsg.createdLt,
                    grmInMsg.bodyHash,
                    if (grmTx.inMsg.msgText.length <= inMsgTextMaxLength)
                        grmTx.inMsg.msgText else msgTextOversizeValue
                )
                //save grmTx
                val grmEntity = grmTxService.add(grmTx.utime, inMsgEntity, txEntity)
                //save outMsgs
                for (outMsg in grmTx.outMsg) {
                    grmOutMsgService.add(
                        outMsg.source, outMsg.destination, outMsg.value,
                        outMsg.fwdFee, outMsg.ihrFee, outMsg.createdLt,
                        outMsg.bodyHash,
                        if (outMsg.msgText.length <= outMsgTextMaxLength)
                            outMsg.msgText else msgTextOversizeValue,
                        grmEntity
                    )
                }
                if (grmTx.inMsg.msgText.length <= inMsgTextMaxLength)
                    emitter.onNext(tx)
            }

        }
    }
}