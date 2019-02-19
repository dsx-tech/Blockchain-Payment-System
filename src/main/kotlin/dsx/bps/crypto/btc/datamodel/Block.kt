package dsx.bps.crypto.btc.datamodel

import java.util.Date
import java.math.BigDecimal
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient

abstract class Block: BitcoindRpcClient.Block {
    abstract val hash: String
    abstract val previousblockhash: String
    abstract val confirmations: Int
    abstract val tx: List<String>

    // for use bitcoin-rpc-client library
    override fun chainwork(): String {
        TODO("not implemented")
    }

    override fun version(): Int {
        TODO("not implemented")
    }

    override fun time(): Date {
        TODO("not implemented")
    }

    override fun mapBigDecimal(key: String?): BigDecimal {
        TODO("not implemented")
    }

    override fun previous(): BitcoindRpcClient.Block {
        TODO("not implemented")
    }

    override fun mapBool(key: String?): Boolean {
        TODO("not implemented")
    }

    override fun confirmations(): Int {
        TODO("not implemented")
    }

    override fun next(): BitcoindRpcClient.Block {
        TODO("not implemented")
    }

    override fun hash(): String {
        TODO("not implemented")
    }

    override fun mapLong(key: String?): Long {
        TODO("not implemented")
    }

    override fun merkleRoot(): String {
        TODO("not implemented")
    }

    override fun difficulty(): BigDecimal {
        TODO("not implemented")
    }

    override fun mapHex(key: String?): ByteArray {
        TODO("not implemented")
    }

    override fun size(): Int {
        TODO("not implemented")
    }

    override fun height(): Int {
        TODO("not implemented")
    }

    override fun previousHash(): String {
        TODO("not implemented")
    }

    override fun bits(): String {
        TODO("not implemented")
    }

    override fun mapDate(key: String?): Date {
        TODO("not implemented")
    }

    override fun mapStr(key: String?): String {
        TODO("not implemented")
    }

    override fun nonce(): Long {
        TODO("not implemented")
    }

    override fun mapInt(key: String?): Int {
        TODO("not implemented")
    }

    override fun tx(): MutableList<String> {
        TODO("not implemented")
    }

    override fun nextHash(): String {
        TODO("not implemented")
    }
}