package dsx.bps.crypto.btc.datamodel

import java.util.Date
import java.math.BigDecimal
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient

abstract class Tx: BitcoindRpcClient.Transaction {
    abstract val txid: String // transaction hash

    // for use bitcoin-rpc-client library
    override fun blockHash(): String {
        TODO("not implemented")
    }

    override fun time(): Date {
        TODO("not implemented")
    }

    override fun mapBigDecimal(key: String?): BigDecimal {
        TODO("not implemented")
    }

    override fun blockIndex(): Int {
        TODO("not implemented")
    }

    override fun blockTime(): Date {
        TODO("not implemented")
    }

    override fun timeReceived(): Date {
        TODO("not implemented")
    }

    override fun mapBool(key: String?): Boolean {
        TODO("not implemented")
    }

    override fun confirmations(): Int {
        TODO("not implemented")
    }

    override fun commentTo(): String {
        TODO("not implemented")
    }

    override fun fee(): BigDecimal {
        TODO("not implemented")
    }

    override fun mapLong(key: String?): Long {
        TODO("not implemented")
    }

    override fun category(): String {
        TODO("not implemented")
    }

    override fun comment(): String {
        TODO("not implemented")
    }

    override fun address(): String {
        TODO("not implemented")
    }

    override fun mapHex(key: String?): ByteArray {
        TODO("not implemented")
    }

    override fun amount(): BigDecimal {
        TODO("not implemented")
    }

    override fun generated(): Boolean {
        TODO("not implemented")
    }

    override fun raw(): BitcoindRpcClient.RawTransaction {
        TODO("not implemented")
    }

    override fun account(): String {
        TODO("not implemented")
    }

    override fun mapDate(key: String?): Date {
        TODO("not implemented")
    }

    override fun mapStr(key: String?): String {
        TODO("not implemented")
    }

    override fun mapInt(key: String?): Int {
        TODO("not implemented")
    }

    override fun txId(): String {
        TODO("not implemented")
    }
}