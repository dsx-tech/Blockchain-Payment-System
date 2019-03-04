package dsx.bps.crypto.btc.datamodel

import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient

data class BtcListSinceBlock(
    val transactions: List<BtcTxSinceBlock>,
    val removed: List<BtcTxSinceBlock>,
    val lastblock: String
): BitcoindRpcClient.TransactionsSinceBlock {

    // for use bitcoin-rpc-client library
    override fun lastBlock(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun transactions(): MutableList<BitcoindRpcClient.Transaction> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}