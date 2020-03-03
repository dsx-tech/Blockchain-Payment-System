package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi

data class GrmInternalTxId(
    val lt: Long,
    val hash: ByteArray
) {

    constructor(transactionId: TonApi.InternalTransactionId) : this(
        transactionId.lt, transactionId.hash
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GrmInternalTxId

        if (lt != other.lt) return false
        if (!hash.contentEquals(other.hash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lt.hashCode()
        result = 31 * result + hash.contentHashCode()
        return result
    }
}