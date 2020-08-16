package dsx.bps.crypto.eth.ethereum

import dsx.bps.core.datamodel.Tx
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

class EthRouter (val ethCoin: EthCoin){
    private val requiredGasForTx: BigInteger = BigInteger.valueOf(21000)
    private val gasPrice = ethCoin.getGasPrice()
    fun resend(tx: Tx) {
        val amount = tx.amount()
        val realAmount = amount - Convert.fromWei(BigDecimal(requiredGasForTx*gasPrice), Convert.Unit.ETHER)

        val account = ethCoin.getAccountByAddress(tx.destination())

        Thread {
            ethCoin.send(account, ethCoin.getSystemAddress(), realAmount, requiredGasForTx)
        }.start()
    }
}