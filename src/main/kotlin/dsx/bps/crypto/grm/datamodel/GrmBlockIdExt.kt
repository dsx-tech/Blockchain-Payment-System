package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi

data class GrmBlockIdExt(
    val workchain: Int,
    val shard: Long,
    val seqno: Int,
    val rootHash: String,
    val fileHash: String
) {
    constructor(tonBlockIdExt: TonApi.TonBlockIdExt) : this(
        tonBlockIdExt.workchain, tonBlockIdExt.shard,
        tonBlockIdExt.seqno,
        byteArrayToHex(tonBlockIdExt.rootHash),
        byteArrayToHex(tonBlockIdExt.fileHash)
    )
}