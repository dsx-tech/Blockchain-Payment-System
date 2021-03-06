package dsx.bps.crypto.btc

import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.BtcService
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import kotlin.concurrent.timer

class BtcExplorer(override val coin: BtcCoin, txServ: TxService, frequency: Long): Explorer(frequency) {

    override val currency: Currency = coin.currency

    private val btcService = BtcService()
    private val txService = txServ

    init {
        explore()
    }

    override fun explore() {
        var last = coin.getBestBlockHash()
        viewed.add(last)

        timer(this::class.toString(), true, 0, frequency) {
            var new = coin.getBestBlockHash()
            if (last != new) {
                last = new
                while (!viewed.contains(new)) {
                    viewed.add(new)
                    val block = coin.getBlock(new)
                    new = block.previousblockhash
                }
                coin.listSinceBlock(new)
                    .transactions
                    .forEach {
                        val tx = coin.constructTx(it)
                        if (txService.checkCryptoAddress(tx)) {
                            val newTx = txService.add(
                                tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                                tx.fee(), tx.hash(), tx.index(), tx.currency()
                            )
                            btcService.add(it.confirmations, it.address, newTx)
                            emitter.onNext(tx)
                        }
                    }
            }
        }
    }
}