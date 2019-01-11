package dsx.bps.kotlin.bitcoin

import java.math.BigDecimal
import dsx.bps.kotlin.core.Block
import dsx.bps.kotlin.core.Tx
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient
import java.util.*

class BtcJSON {

    data class BtcBlock(
        val confirmations: Int,
        override val hash: String,
        val height: Int,
        val merkleroot: String,
        val nTx: Int,
        val nonce: Long,
        val previousblockhash: String,
        val bestblockhash: String,
        val tx: List<BtcRawTx>,

        val bits: String,
        val mediantime: Int,
        val time: Int,
        val version: Int,
        val chainwork: String,
        val size: Int,
        val strippedsize: Int,
        val versionHex: String,
        val weight: Int,
        val difficulty: BigDecimal
    ): Block() {
        override fun equals(other: Any?): Boolean {
            if (other == null || other !is BtcBlock) {
                return false
            } // native implementation for now
            return this.hash == other.hash
        }
    }

    data class BtcRawTx(
        override val txid: String,
        val hash: String,
        val vout: List<Vout>,
        val hex: String,

        val locktime: Long,
        val version: Int,
        val vin: List<Vin>,
        val size: Long,
        val vsize: Long,
        val weight: Int
    ): Tx()

    data class Vin(
        val scriptSig: ScriptSig,
        val sequence: Long,
        val txid: String,
        val txinwitness: List<String>,
        val vout: Int
    )

    data class ScriptSig(
        val asm: String,
        val hex: String
    )

    data class Vout(
        val n: Int,
        val scriptPubKey: ScriptPubKey,
        val value: BigDecimal
    )

    data class ScriptPubKey(
        val asm: String,
        val hex: String,
        val type: String,
        val reqSigs: Int? = null,
        val addresses: List<String>? = null
    )

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
    ): Tx(), BitcoindRpcClient.Transaction {
        override fun blockHash(): String {
            return blockhash
        }

        override fun time(): Date {
            return Date(time)
        }

        override fun mapBigDecimal(key: String?): BigDecimal {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun blockIndex(): Int {
            return blockindex
        }

        override fun blockTime(): Date {
            return Date(blocktime)
        }

        override fun timeReceived(): Date {
            return Date(timereceived)
        }

        override fun mapBool(key: String?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun confirmations(): Int {
            return confirmations
        }

        override fun commentTo(): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun fee(): BigDecimal {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun mapLong(key: String?): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun category(): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun comment(): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun address(): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun mapHex(key: String?): ByteArray {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun amount(): BigDecimal {
            return amount
        }

        override fun generated(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun raw(): BitcoindRpcClient.RawTransaction {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun account(): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun mapDate(key: String?): Date {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun mapStr(key: String?): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun mapInt(key: String?): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun txId(): String {
            return txid
        }
    }

    data class BtcTxDetail(
        val address: String,
        val amount: BigDecimal,
        val category: String,
        val label: String,
        val vout: Int
    )

    data class BtcTxOutput(
        val address: String,
        val amount: BigDecimal
    ): BitcoindRpcClient.TxOutput {
        override fun amount(): BigDecimal {
            return amount
        }

        override fun data(): ByteArray? {
            return null
        }

        override fun address(): String {
            return address
        }
    }

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