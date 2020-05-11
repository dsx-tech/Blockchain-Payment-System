package dsx.bps.crypto.eth.ethereum

import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.EthService
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import dsx.bps.crypto.eth.Router
import org.web3j.protocol.core.methods.response.EthBlock
import org.web3j.protocol.core.methods.response.Transaction
import java.math.BigInteger
import java.util.*
import kotlin.concurrent.timer

class EthExplorer(override val coin: EthCoin, frequency: Long, datasource: Datasource,
                  private val txService: TxService): Explorer(frequency) {

    override val currency: Currency = coin.currency
    private val ethService = EthService(datasource)
    private var router: Router
    private lateinit var last: EthBlock.Block

    private lateinit var ethTimer : Timer

    init {
        explore()
        val r =  Router.getRouterByAddress(coin.getSystemAddress())
        if (r == null) {
           router = Router()
           Router.addRouter(coin.getSystemAddress(), router)
        }
        else
        {
            router = r
        }
        router.ethRouter = EthRouter(coin)
    }

    override fun explore() {
        last = coin.getLatestBlock()
        var scannedBlocks = 0
        while (last.number != BigInteger.ZERO && scannedBlocks < coin.scanningCount) {
            last = coin.getBlockByHash(last.parentHash)
            scannedBlocks ++
        }

        viewed.add(last.hash)

        ethTimer = timer(this::class.toString(), true, 0, frequency) {
            var new = coin.getLatestBlock()
            if (last.hash != new.hash) {
                val lastCandidate = new
                while (!viewed.contains(new.hash)) {
                    new.transactions
                        .forEach {
                            val transaction = coin.constructTx(it.get() as Transaction)
                            val txs = txService.add(transaction.status(), transaction.destination(), "",
                                transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())
                            var nonce: Long
                            if ((it.get() as Transaction).nonceRaw == "0x0")
                            {
                                nonce = 0
                            }
                            else
                            {
                                nonce = (it.get() as Transaction).nonce.toLong()
                            }
                            ethService.addEthTransaction( (it.get() as Transaction).from, nonce, txs, "")
                            emitter.onNext(transaction)
                            Thread {
                                router.resendTx(transaction)
                            }.start()

                        }
                    viewed.add(new.hash)
                    new = coin.getBlockByHash(new.parentHash)
                }
                last = lastCandidate
            }

        }
    }

    fun kill(){
        this.ethTimer.cancel()
    }

}