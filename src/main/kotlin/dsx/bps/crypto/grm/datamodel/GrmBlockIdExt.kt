package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi

data class GrmBlockIdExt(
    val workchain: Int,
    val shard: Long,
    val seqno: Int,
    val rootHash: ByteArray,
    val fileHash: ByteArray
) {

    constructor(tonBlockIdExt: TonApi.TonBlockIdExt) : this(
        tonBlockIdExt.workchain, tonBlockIdExt.shard,
        tonBlockIdExt.seqno, tonBlockIdExt.rootHash,
        tonBlockIdExt.fileHash
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GrmBlockIdExt

        if (workchain != other.workchain) return false
        if (shard != other.shard) return false
        if (seqno != other.seqno) return false
        if (!rootHash.contentEquals(other.rootHash)) return false
        if (!fileHash.contentEquals(other.fileHash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workchain
        result = 31 * result + shard.hashCode()
        result = 31 * result + seqno
        result = 31 * result + rootHash.contentHashCode()
        result = 31 * result + fileHash.contentHashCode()
        return result
    }
}