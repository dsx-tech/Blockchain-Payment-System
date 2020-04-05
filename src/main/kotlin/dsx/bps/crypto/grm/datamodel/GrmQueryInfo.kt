package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi

data class GrmQueryInfo(
    val id: Long,
    val validUntil: Long,
    val bodyHash: String
) {
    constructor(queryInfo: TonApi.QueryInfo) : this(
        queryInfo.id, queryInfo.validUntil, byteArrayToHex(queryInfo.bodyHash)
    )
}