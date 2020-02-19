package dsx.bps.crypto.trx

import dsx.bps.DBservices.TrxService
import dsx.bps.DBservices.TxService
import dsx.bps.config.DatabaseConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import kotlin.concurrent.timer

class TrxExplorer(override val coin: TrxCoin, frequency: Long): Explorer(frequency) {

    override val currency: Currency = coin.currency

    init {
        explore()
    }

    override fun explore() {
        var last = coin.getNowBlock()
        viewed.add(last.hash)

        timer(this::class.toString(), true, 0, frequency) {
            var new = coin.getNowBlock()
            if (last.hash != new.hash) {
                last = new
                while (!viewed.contains(new.hash)) {
                    new.transactions
                        .forEach {
                            val tx = coin.constructTx(it)
                            val new = TxService(coin.config[DatabaseConfig.connectionURL], coin.config[DatabaseConfig.driver]).add(tx.status().toString(), tx.destination(), tx.tag(), tx.amount(),
                                tx.fee(), tx.hash(), tx.index(), tx.currency().toString())
                            TrxService(coin.config[DatabaseConfig.connectionURL], coin.config[DatabaseConfig.driver]).add(new)
                            emitter.onNext(tx)
                        }
                    viewed.add(new.hash)
                    new = coin.getBlockById(new.blockHeader.rawData.parentHash)
                }
            }
        }

    }
}