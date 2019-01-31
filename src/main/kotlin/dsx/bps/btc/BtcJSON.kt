package dsx.bps.btc

import java.util.Date
import java.math.BigDecimal
import dsx.bps.primitives.Tx
import dsx.bps.primitives.Block
import dsx.bps.primitives.TxOut
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient

class BtcJSON {

    data class BtcBlock(
        override val hash: String,
        override val confirmations: Int,
        override val previousblockhash: String,
        override val tx: List<String>
    ): Block()

    data class BtcTx(
        override val txid: String,
        val amount: BigDecimal,
        val confirmations: Int,
        val blockhash: String,
        val details: List<BtcTxDetail>,
        val hex: String,
        val time: Long,

        val bip125_replaceable: String,
        val blockindex: Int,
        val blocktime: Long,
        val timereceived: Long,
        val walletconflicts: List<Any>
    ): Tx()

    data class BtcTxDetail(
        val address: String,
        val amount: BigDecimal,
        val category: String,
        val label: String,
        val vout: Int
    )

    data class BtcTxOutput(
        override val address: String,
        override val amount: BigDecimal
    ): TxOut()

    data class BtcListSinceBlock(
        val transactions: List<BtcTxSinceBlock>,
        val removed: List<BtcTxSinceBlock>,
        val lastblock: String
    ): BitcoindRpcClient.TransactionsSinceBlock {
        override fun lastBlock(): String {
            return lastblock
        }

        override fun transactions(): MutableList<BitcoindRpcClient.Transaction> {
            TODO("not implemented")
        }
    }

    data class BtcTxSinceBlock(
        override val txid: String,
        val confirmations: Int,
        val address: String,
        val amount: BigDecimal,
        val blockhash: String,
        val category: String,

        val abandoned: Boolean,
        val bip125_replaceable: String,
        val blockindex: Int,
        val blocktime: Int,
        val fee: BigDecimal,
        val time: Int,
        val timereceived: Int,
        val vout: Int,
        val walletconflicts: List<Any>
): Tx()
}