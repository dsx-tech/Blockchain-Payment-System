package primitives

import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient
import java.math.BigDecimal

abstract class TxOutput: BitcoindRpcClient.TxOutput {

    abstract val address: String
    abstract val amount: BigDecimal

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