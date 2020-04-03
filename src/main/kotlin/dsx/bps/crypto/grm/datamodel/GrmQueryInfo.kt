package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi

data class GrmQueryInfo(
    val id: Long,
    val validUntil: Long,
    val bodyHash: ByteArray
) {
    constructor(queryInfo: TonApi.QueryInfo) : this(
        queryInfo.id, queryInfo.validUntil, queryInfo.bodyHash
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GrmQueryInfo

        if (id != other.id) return false
        if (validUntil != other.validUntil) return false
        if (!bodyHash.contentEquals(other.bodyHash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + validUntil.hashCode()
        result = 31 * result + bodyHash.contentHashCode()
        return result
    }
}