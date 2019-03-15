package dsx.bps.crypto.xrp.datamodel

data class XrpAccountTx(
    val tx: XrpTx,
    val validated: Boolean
) {
    val type: String = tx.type
}