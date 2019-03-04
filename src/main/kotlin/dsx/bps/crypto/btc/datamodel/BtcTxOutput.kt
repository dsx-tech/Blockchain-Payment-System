package dsx.bps.crypto.btc.datamodel

import java.math.BigDecimal
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient

data class BtcTxOutput(
    val address: String,
    val amount: BigDecimal
): BitcoindRpcClient.TxOutput {

    // for use bitcoin-rpc-client library
    override fun address() = address

    override fun amount() = amount

    override fun data() = null
}
