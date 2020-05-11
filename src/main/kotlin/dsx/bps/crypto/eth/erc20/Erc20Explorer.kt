package dsx.bps.crypto.eth.erc20

import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.EthService
import dsx.bps.core.datamodel.Currency
import dsx.bps.crypto.common.Explorer
import dsx.bps.crypto.eth.Router
import dsx.bps.crypto.eth.erc20.datamodel.ERC20
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter

class Erc20Explorer (override val coin: Erc20Coin, private val token: ERC20, frequency: Long, datasource: Datasource,
                     private val txService: TxService): Explorer(frequency) {

    override val currency: Currency = coin.currency
    private val ethService = EthService(datasource)
    private val router: Router

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
        router.erc20Router = Erc20Router(coin)
    }

    override fun explore() {
        val filter = EthFilter(
            DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST,
            coin.contract.address
        )
        token.transferEventFlowable(filter).subscribe { response ->
            val txHash = response.log!!.transactionHash

            val doubleAmount = coin.convertAmountToDecimal(response.tokens!!)
            val transaction = coin.constructTx(txHash, response.to!!, doubleAmount)

            router.resendTx(transaction)
            emitter.onNext(transaction)
            if (response.from != coin.getSystemAddress() || response.to == coin.contract.owner)
            {
                val txs = txService.add(transaction.status(), transaction.destination(),"",
                    transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())

                ethService.addEthTransaction(coin.getSystemAddress(), coin.getTransactionByHash(txHash).nonce.toLong(), txs,
                    coin.contract.address)
            }
        }
    }

}