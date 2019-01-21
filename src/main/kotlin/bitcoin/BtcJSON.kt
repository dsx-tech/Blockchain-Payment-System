package dsx.bps.kotlin.bitcoin

import java.math.BigDecimal
import dsx.bps.kotlin.core.Block
import dsx.bps.kotlin.core.Tx
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient
import java.util.*

class BtcJSON {

    data class BtcBlock(
        val bits: String,
        val confirmations: Int,
        override val hash: String,
        val height: Int,
        val mediantime: Int,
        val merkleroot: String,
        val nTx: Int,
        val nonce: Long,
        val previousblockhash: String,
        val time: Int,
        val tx: List<BtcTx>,
        val version: Int

//        val chainwork: String,
//        val size: Int,
//        val strippedsize: Int,
//        val versionHex: String,
//        val weight: Int
//        @Json(ignored = true)
//        val difficulty: BigDecimal
    ): Block() {
        override fun equals(other: Any?): Boolean {
            if (other == null || other !is BtcBlock) {
                return false
            } // native implementation for now
            return this.hash == other.hash
        }
    }

    data class BtcTx(
        override val txid: String,
        val hash: String,
        val hex: String,
        val locktime: Long,
        val version: Int,
        val vout: List<Vout>

//        val vin: List<Vin>,
//        val size: Long,
//        val vsize: Long,
//        val weight: Int
    ): Tx(), BitcoindRpcClient.Transaction {
        override fun blockHash(): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun time(): Date {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun mapBigDecimal(key: String?): BigDecimal {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun blockIndex(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun blockTime(): Date {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun timeReceived(): Date {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun mapBool(key: String?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun confirmations(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

//    data class Vin(
//        val scriptSig: ScriptSig,
//        val sequence: Long,
//        val txid: String,
//        val txinwitness: List<String>,
//        val vout: Int
//    )

//    data class ScriptSig(
//        val asm: String,
//        val hex: String
//    )

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
    val removed: List<Any>,
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
    val txid: String,
    val address: String,
    val amount: Double,
    val blockhash: String,
    val category: String
//    val abandoned: Boolean,
//    val bip125_replaceable: String,
//    val blockindex: Int,
//    val blocktime: Int,
//    val confirmations: Int,
//    val fee: Double,
//    val time: Int,
//    val timereceived: Int,
//    val vout: Int,
//    val walletconflicts: List<Any>
)
}