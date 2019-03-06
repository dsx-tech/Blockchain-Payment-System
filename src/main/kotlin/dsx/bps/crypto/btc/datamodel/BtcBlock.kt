package dsx.bps.crypto.btc.datamodel

data class BtcBlock(
    val hash: String,
    val confirmations: Int,
    val previousblockhash: String,
    val tx: List<String>
)