package dsx.bps.crypto.grm

import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.core.tx.TxTableConstant.tagMaxLength
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
        override val coin: GrmCoin, grmInMsgServ: GrmInMsgService,
        grmOutMsgServ: GrmOutMsgService, grmTxServ: GrmTxService,
        txServ: TxService, frequency: Long
) : Explorer(frequency) {

    override val currency: Currency = coin.currency

    var syncUtime: Long = -1

    private val grmInMsgService = grmInMsgServ
    private val grmOutMsgService = grmOutMsgServ
    private val grmTxService = grmTxServ
    private val txService = txServ

    init {
        explore()
    }

    override fun explore() {
        var lastTxId: GrmInternalTxId = if (!grmTxService.tableIsEmpty()) {
            val grmNewestKnownTx: TxEntity = grmTxService.getGrmNewestKnownTx()
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
                        grmInMsg.bodyHash, grmInMsg.msgText
                )
                //save grmTx
                grmTxService.add(grmTx.utime, inMsg, newTx)
            } else if (grmTx.outMsg.size == 1) {
                // if grmTx has one outMsg
                val grmInMsg = grmTx.inMsg
                val grmOutMsg = grmTx.outMsg.single()
                //save inMsg
                val inMsg = grmInMsgService.add(
                        grmInMsg.source, grmInMsg.destination, grmInMsg.value,
                        grmInMsg.fwdFee, grmInMsg.ihrFee, grmInMsg.createdLt,
                        grmInMsg.bodyHash, grmInMsg.msgText
                )
                //save Tx
                val tx = coin.constructPaymentTx(
                        grmTx.transactionId.hash, grmTx.transactionId.lt,
                        BigDecimal(grmOutMsg.value), grmOutMsg.destination, grmOutMsg.msgText,
                        BigDecimal(grmOutMsg.fwdFee + grmOutMsg.ihrFee), TxStatus.CONFIRMED
                )
                val newTx = txService.add(
                        tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                        tx.fee(), tx.txid().hash, tx.txid().index, tx.currency()
                )
                //save grmTx
                val grmEntity = grmTxService.add(grmTx.utime, inMsg, newTx)
                //save outMsg
                if (grmOutMsg.msgText.length <= tagMaxLength) {
                    grmOutMsgService.add(
                            grmOutMsg.source, grmOutMsg.destination, grmOutMsg.value,
                            grmOutMsg.fwdFee, grmOutMsg.ihrFee, grmOutMsg.createdLt,
                            grmOutMsg.bodyHash, grmOutMsg.msgText, grmEntity
                    )
                }
            } else if (grmTx.outMsg.size > 1) {
                // if grmTx has many outMsg
                val grmInMsg = grmTx.inMsg
                //save inMsg
                val inMsg = grmInMsgService.add(
                        grmInMsg.source, grmInMsg.destination, grmInMsg.value,
                        grmInMsg.fwdFee, grmInMsg.ihrFee, grmInMsg.createdLt,
                        grmInMsg.bodyHash, grmInMsg.msgText
                )
                //save Tx
                val tx = coin.constructPaymentTx(
                        grmTx.transactionId.hash, grmTx.transactionId.lt,
                        BigDecimal.ZERO, "", "",
                        BigDecimal.ZERO, TxStatus.INCORRECT
                )
                val newTx = txService.add(
                        tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                        tx.fee(), tx.txid().hash, tx.txid().index, tx.currency()
                )
                //save grmTx
                val grmEntity = grmTxService.add(grmTx.utime, inMsg, newTx)
                //save outMsgs
                for (grmOutMsg in grmTx.outMsg) {
                    if (grmOutMsg.msgText.length <= tagMaxLength) {
                        grmOutMsgService.add(
                                grmOutMsg.source, grmOutMsg.destination, grmOutMsg.value,
                                grmOutMsg.fwdFee, grmOutMsg.ihrFee, grmOutMsg.createdLt,
                                grmOutMsg.bodyHash, grmOutMsg.msgText, grmEntity
                        )
                    }
                }
            }
        } else if (grmTx.inMsg.source != "" && grmTx.inMsg.msgText.length <= tagMaxLength) {
            // if tx process internal message - save tx in db and emit it
            //save tx
            val tx = coin.constructDepositTx(grmTx)
            val newTx = txService.add(
                    tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                    tx.fee(), tx.txid().hash, tx.txid().index, tx.currency()
            )
            val grmInMsg = grmTx.inMsg
            //save inMsg
            val inMsg = grmInMsgService.add(
                    grmInMsg.source, grmInMsg.destination, grmInMsg.value,
                    grmInMsg.fwdFee, grmInMsg.ihrFee, grmInMsg.createdLt,
                    grmInMsg.bodyHash, grmInMsg.msgText
            )
            //save grmTx
            val grmEntity = grmTxService.add(grmTx.utime, inMsg, newTx)
            //save outMsgs
            for (outMsg in grmTx.outMsg) {
                if (outMsg.msgText.length <= tagMaxLength) {
                    grmOutMsgService.add(
                            outMsg.source, outMsg.destination, outMsg.value,
                            outMsg.fwdFee, outMsg.ihrFee, outMsg.createdLt,
                            outMsg.bodyHash, outMsg.msgText, grmEntity
                    )
                }
            }
            emitter.onNext(tx)
        }
    }
}