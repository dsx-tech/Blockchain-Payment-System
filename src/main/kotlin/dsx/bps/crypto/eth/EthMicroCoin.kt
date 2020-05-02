package dsx.bps.crypto.eth

import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.EthService
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.eth.datamodel.EthAccount
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

class EthMicroCoin (private val connector: EthRpc, private val systemAddress: String, private val confirmations: Int,
                    private val ethService: EthService, private val txService: TxService) {
    val requiredGasForTx = BigInteger.valueOf(21000)
    val gasPrice = connector.getGasPrice()

    fun resend(account: EthAccount) {
        var nonce = ethService.getLatestNonce(account.address)
        if (nonce == null)
        {
            nonce = connector.getTransactionCount(account.address)
        }
        else
        {
            nonce ++
        }

        val amount = connector.getBalance(account.address)
        val realAmount = amount - Convert.fromWei(BigDecimal(requiredGasForTx*gasPrice), Convert.Unit.ETHER)
        val rawTransaction = connector.createRawTransaction(nonce, gasPrice, requiredGasForTx,
            systemAddress, realAmount)
          val credentials = WalletUtils.loadCredentials(account.password, account.wallet)
          val signedTransaction = connector.signTransaction(rawTransaction, credentials)
        Thread {
           val resultHash = connector.sendTransaction(signedTransaction)

            val transaction = constructTx(connector.getTransactionByHash(resultHash))

            val txs = txService.add(transaction.status(), systemAddress,"",
                transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())
            ethService.add(account.address, nonce.toLong(), txs)
        }.start()
    }

    private fun constructTx(ethTx: Transaction): Tx {
        return object: Tx {
            override fun currency() = Currency.ETH

            override fun hash() = ethTx.hash

            override fun amount() = Convert.fromWei(ethTx.value.toBigDecimal(), Convert.Unit.ETHER)

            override fun destination() = ethTx.to

            override fun fee(): BigDecimal {
                return if (this.status() == TxStatus.VALIDATING) {
                    Convert.fromWei(ethTx.gasPrice.multiply(ethTx.gas).toBigDecimal(), Convert.Unit.ETHER)
                } else {
                    Convert.fromWei(ethTx.gasPrice
                        .multiply(connector.getTransactionReceiptByHash(ethTx.hash).gasUsed).toBigDecimal(),
                        Convert.Unit.ETHER)
                }
            }

            override fun status(): TxStatus {
                val latestBlock = connector.getLatestBlock()
                if (ethTx.blockHash == null) {
                    return TxStatus.VALIDATING
                } else {
                    val conf = latestBlock.number - ethTx.blockNumber
                    if (conf < confirmations.toBigInteger()) {
                        return TxStatus.VALIDATING
                    } else {
                        return TxStatus.CONFIRMED
                    }
                }
            }
        }
    }

}