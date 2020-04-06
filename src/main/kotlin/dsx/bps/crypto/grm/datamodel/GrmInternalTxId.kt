package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi


data class GrmInternalTxId(
    val lt: Long,
    val hash: String
) {
    constructor(transactionId: TonApi.InternalTransactionId) : this(
        transactionId.lt,
        byteArrayToHex(transactionId.hash)
    )
}