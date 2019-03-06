package dsx.bps.crypto.btc.datamodel

data class BtcListSinceBlock(
    val transactions: List<BtcTxSinceBlock>,
    val removed: List<BtcTxSinceBlock>,
    val lastblock: String
)