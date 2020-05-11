package dsx.bps.crypto.eth

import dsx.bps.core.datamodel.Tx
import dsx.bps.crypto.eth.erc20.Erc20Router
import dsx.bps.crypto.eth.ethereum.EthRouter

class Router {
    companion object {
        private val routers = mutableMapOf<String, Router>()

        fun getRouterByAddress(address: String): Router? {
            return if (routers.containsKey(address))
            {
                routers.getValue(address)
            }
            else
            {
                null
            }

        }

        fun addRouter(address: String, router: Router) {
            routers[address] = router
        }
    }

    var ethRouter: EthRouter? = null
    var erc20Router: Erc20Router? = null

    fun resendTx(tx: Tx) {
        val ethAccount = ethRouter!!.ethCoin.accounts.find { elem -> elem.address == tx.destination() }
        if (ethAccount != null)
        {
            ethRouter!!.resend(tx)
        }

        if (erc20Router != null)
        {
            val erc20Account = erc20Router!!.erc20Coin.accounts.find { elem -> elem.address == tx.destination() }
            if (erc20Account != null)
            {
                erc20Router!!.resend(tx)
            }
        }
    }

}
