package dsx.bps.crypto.eth.erc20

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import java.math.BigDecimal

class Erc20Router(val coin: Erc20Coin) {
    fun resend(tx: Tx) {
        val ercAccount = coin.accounts[tx.destination()]!!
        if (!ercAccount.second)
        {
            this.sendMoneyForApprove(tx)
            val receipt = coin.approveFrom(ercAccount.first)
            coin.accounts[ercAccount.first.address] = Pair(ercAccount.first, true)

            val transaction = coin.constructTx(receipt.transactionHash, coin.getSystemAddress(), BigDecimal.ZERO)
            val txs = coin.txService.add(transaction.status(), transaction.destination(),"",
                transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())
            coin.ethService.addEthTransaction(ercAccount.first.address,
                coin.getTransactionByHash(receipt.transactionHash).nonce.toLong(), txs, coin.contract.address)

        }
        val txInfo = coin.transferAllFundsFrom(ercAccount.first.address)
        val txHash = txInfo.first.transactionHash
        val transaction = coin.constructTx(txHash, coin.getSystemAddress(), txInfo.second)

        val txs = coin.txService.add(transaction.status(), transaction.destination(),"",
            transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())
        coin.ethService.addEthTransaction(coin.getSystemAddress(),
            coin.getTransactionByHash(txHash).nonce.toLong(), txs, coin.contract.address)

    }

    fun sendMoneyForApprove(tx: Tx) {
        val requiredEth = Convert.fromWei(BigDecimal(DefaultGasProvider.GAS_LIMIT*DefaultGasProvider.GAS_PRICE), Convert.Unit.ETHER)
        val hash = coin.send(to = tx.destination(), amount = requiredEth)
        val ethTx = coin.getTransactionByHash(hash)
        val transaction = coin.constructTx(hash, tx.destination(), requiredEth, Currency.ETH)

        val txs = coin.txService.add(transaction.status(), transaction.destination(),"",
            transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())
        coin.ethService.addEthTransaction(coin.getSystemAddress(), ethTx.nonce.toLong(), txs, "")

        var count = 0
        while(coin.getTransactionByHash(hash).blockHash == null)
        {
            Thread.sleep(2000)
            if (count == 100)
            {
                throw Exception("Timeout exceeded")
            }
            count++
        }
    }
}