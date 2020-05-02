package dsx.bps.crypto.eth

import dsx.bps.crypto.eth.datamodel.EthAccount

class EthRouter (private val microCoin: EthMicroCoin){
    private val accounts = mutableListOf<EthAccount>()

    fun addAccount(account: EthAccount) {
        accounts.add(account)
    }

    fun checkAddress(address: String) {
        val wallet = accounts.find { elem -> elem.address == address }
        if (wallet != null)
        {
            microCoin.resend(wallet)
        }
    }
}