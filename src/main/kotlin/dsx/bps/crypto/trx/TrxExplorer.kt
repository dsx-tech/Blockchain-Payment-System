package dsx.bps.crypto.trx

import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.TrxService
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import kotlin.concurrent.timer

class TrxExplorer(override val coin: TrxCoin, txServ: TxService, frequency: Long): Explorer(frequency) {

    override val currency: Currency = coin.currency

    private val trxService = TrxService()
    private val txService = txServ

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
                            if (txService.checkCryptoAddress(tx)) {
                                val newTx = txService.add(
                                    tx.status(), tx.destination(), tx.paymentReference(), tx.amount(),
                                    tx.fee(), tx.hash(), tx.index(), tx.currency()
                                )
                                trxService.add(it.rawData.contract.first().parameter.value.ownerAddress,
                                    it.ret.map { trxTxRet -> trxTxRet.contractRet },  newTx)
                                emitter.onNext(tx)
                            }
                        }
                    viewed.add(new.hash)
                    new = coin.getBlockById(new.blockHeader.rawData.parentHash)
                }
            }
        }

    }
}