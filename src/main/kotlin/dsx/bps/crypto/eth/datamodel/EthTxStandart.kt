package dsx.bps.crypto.eth.datamodel

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.eth.EthRpc
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.utils.Convert
import java.math.BigDecimal

class EthTxStandart(val ethTx : Transaction, val rpc : EthRpc, val confirmations : Int) : Tx{
    override fun currency() = Currency.ETH

    override fun hash() = ethTx.hash

    override fun amount() = Convert.fromWei(ethTx.value.toBigDecimal(), Convert.Unit.ETHER)

    override fun destination() = ethTx.to

    override fun fee(): BigDecimal {
        if (this.status() == TxStatus.VALIDATING)
        {
            return ethTx.gasPrice.multiply(ethTx.gas).toBigDecimal()
        }
        else
        {
            return ethTx.gasPrice.multiply(rpc.getTransactionReceiptByHash(ethTx.hash).gasUsed).toBigDecimal()
        }
    }

    override fun status(): TxStatus {
        val latestBlock = rpc.getLatestBlock()
        val conf = latestBlock.number - ethTx.blockNumber
        if (conf < confirmations.toBigInteger())
        {
            return TxStatus.VALIDATING
        }
        else
        {
            return TxStatus.CONFIRMED
        }
    }
}