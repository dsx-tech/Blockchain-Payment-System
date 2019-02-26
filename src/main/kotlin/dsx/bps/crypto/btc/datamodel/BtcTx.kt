package dsx.bps.crypto.btc.datamodel

import java.util.*
import java.math.BigDecimal
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient

data class BtcTx(
    val amount: BigDecimal,
    val fee: BigDecimal,
    val confirmations: Int,
    val blockhash: String,
    val txid: String,
    val time: Long,
    val details: List<BtcTxDetail>,
    val hex: String
): BitcoindRpcClient.Transaction {

    // for use bitcoin-rpc-client library
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