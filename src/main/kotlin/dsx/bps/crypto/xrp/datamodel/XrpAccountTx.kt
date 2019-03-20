package dsx.bps.crypto.xrp.datamodel

data class XrpAccountTx(
    val tx: XrpTx,
    val meta: XrpTxMeta,
    val validated: Boolean
)