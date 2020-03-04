package dsx.bps.crypto.btc

import dsx.bps.DBservices.BtcService
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.TxService
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import kotlin.concurrent.timer

class BtcExplorer(override val coin: BtcCoin, datasource: Datasource, frequency: Long): Explorer(frequency) {

    override val currency: Currency = coin.currency

    private val btcService = BtcService(datasource)
    private val txService = TxService(datasource)

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
                var blockHash = ""
                while (!viewed.contains(new)) {
                    viewed.add(new)
                    val block = coin.getBlock(new)
                    new = block.previousblockhash
                    blockHash = block.hash
                }
                coin.listSinceBlock(new)
                    .transactions
                    .forEach {
                        val tx = coin.constructTx(it)
                        val newTx = txService.add(
                            tx.status(), tx.destination(), tx.tag(), tx.amount(),
                            tx.fee(), tx.hash(), tx.index(), tx.currency()
                        )
                        btcService.add(it.confirmations, blockHash, it.address, newTx)
                        emitter.onNext(tx)
                    }
            }
        }
    }
}