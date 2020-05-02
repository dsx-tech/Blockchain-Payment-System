package dsx.bps.crypto.eth

import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.EthService
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import org.web3j.protocol.core.methods.response.Transaction
import java.math.BigInteger
import java.util.*
import kotlin.concurrent.timer

class EthExplorer(override val coin: EthCoin, frequency: Long, datasource: Datasource, txServ: TxService): Explorer(frequency) {

    override val currency: Currency = coin.currency
    private val ethService = EthService(datasource)
    private val txService = txServ


    private lateinit var Timer : Timer

    init {
        explore()
    }

    override fun explore() {
        var last = coin.getLatestBlock()
        var scannedBlocks = 0
        while (last.number != BigInteger.ZERO && scannedBlocks < coin.scanningCount) {
            last = coin.getBlockByHash(last.parentHash)
            scannedBlocks ++
        }

        viewed.add(last.hash)

        Timer = timer(this::class.toString(), true, 0, frequency) {
            var new = coin.getLatestBlock()
            if (last.hash != new.hash) {
                val lastCandidate = new
                while (!viewed.contains(new.hash)) {
                    new.transactions
                        .forEach {
                            val transaction = coin.constructTx(it.get() as Transaction)
                            val txs = txService.add(transaction.status(), transaction.destination(), "",
                                transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())
                            ethService.add( (it.get() as Transaction).from, (it.get() as Transaction).nonce.toLong(), txs)
                            emitter.onNext(transaction)
                        }
                    viewed.add(new.hash)
                    new = coin.getBlockByHash(new.parentHash)
                }
                last = lastCandidate
            }
        }
    }

    fun kill(){
        this.Timer.cancel()
    }

}