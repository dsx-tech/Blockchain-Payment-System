package dsx.bps.crypto.xrp

import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.TxService
import dsx.bps.DBservices.XrpService
import dsx.bps.config.DatabaseConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import kotlin.concurrent.timer

class XrpExplorer(override val coin: XrpCoin, datasource: Datasource, txServ: TxService, frequency: Long): Explorer(frequency) {

    override val currency: Currency = coin.currency

    private val xrpService = XrpService(datasource)
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
                        val newTx = txService.add(tx.status(), tx.destination(), tx.tag(), tx.amount(),
                            tx.fee(), tx.hash(), tx.index(), tx.currency())
                        xrpService.add(tx.fee(), it.tx.account, it.tx.sequence, it.validated, newTx)
                        emitter.onNext(tx)
                    }
                lastIndex = newIndex
            }
        }
    }
}