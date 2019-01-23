package core

import primitives.Tx
import primitives.Block
import java.math.BigDecimal
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient

abstract class CoinRPC(url: String): BitcoinJSONRPCClient(url) {

    abstract override fun getBalance(): BigDecimal

    abstract override fun getNewAddress(): String

    abstract override fun getBlock(hash: String): Block

    abstract override fun getTransaction(txId: String?): Tx

    abstract override fun create

}