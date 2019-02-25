package dsx.bps.crypto.btc.datamodel

import java.util.*
import java.math.BigDecimal
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient

data class BtcBlock(
    val hash: String,
    val confirmations: Int,
    val previousblockhash: String,
    val tx: List<String>
): BitcoindRpcClient.Block {

    // for use bitcoin-rpc-client library
    override fun chainwork(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun version(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun time(): Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapBigDecimal(key: String?): BigDecimal {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun previous(): BitcoindRpcClient.Block {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapBool(key: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun confirmations(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun next(): BitcoindRpcClient.Block {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hash(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapLong(key: String?): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun merkleRoot(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun difficulty(): BigDecimal {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapHex(key: String?): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun size(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun height(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun previousHash(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bits(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapDate(key: String?): Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapStr(key: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nonce(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapInt(key: String?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun tx(): MutableList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nextHash(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}