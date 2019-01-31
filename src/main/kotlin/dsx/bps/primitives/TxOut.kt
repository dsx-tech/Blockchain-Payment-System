package dsx.bps.primitives

import java.math.BigDecimal
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient

abstract class TxOut: BitcoindRpcClient.TxOutput {

    abstract val address: String
    abstract val amount: BigDecimal

    // for use bitcoin-rpc-client library
    override fun amount(): BigDecimal {
        TODO("not implemented")
    }

    override fun data(): ByteArray? {
        TODO("not implemented")
    }

    override fun address(): String {
        TODO("not implemented")
    }
}