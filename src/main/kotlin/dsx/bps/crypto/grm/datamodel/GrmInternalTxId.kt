package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi

data class GrmInternalTxId(
    val lt: Long,
    val hash: String
) {
    constructor(transactionId: TonApi.InternalTransactionId) : this(
        transactionId.lt,
        byteArrayToHex(transactionId.hash)
    )
}