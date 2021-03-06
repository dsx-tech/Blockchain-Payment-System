package dsx.bps.crypto.xrp

import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.XrpService
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import kotlin.concurrent.timer

class XrpExplorer(
        override val coin: XrpCoin,
    txServ: TxService, frequency: Long
) : Explorer(frequency) {

    override val currency: Currency = coin.currency

    private val xrpService = XrpService()
    private val txService = txServ

    init {
        explore()
    }

    override fun explore() {
        var lastIndex = coin.getLastLedger().index

        timer(this::class.toString(), true, 0, frequency) {
            val newIndex = coin.getLastLedger().index
            if (lastIndex != newIndex) {
                coin.getAccountTxs(lastIndex + 1, newIndex)
                    .transactions
                    .filter {
                        it.tx.type == "Payment" &&
                                it.tx.amount?.currency == currency.name
                    }
                    .forEach {
                        val tx = coin.constructTx(it)
                        if (txService.checkCryptoAddress(tx)) {
                            val newTx = txService.add(
                                tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                                tx.fee(), tx.hash(), tx.index(), tx.currency()
                            )
                            xrpService.add(tx.fee(), it.tx.account, it.tx.sequence, it.validated, newTx)
                            emitter.onNext(tx)
                        }
                    }
                lastIndex = newIndex
            }
        }
    }
}