package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi

data class GrmMsgData(val body: ByteArray) {

    constructor(msgDataRaw: TonApi.MsgDataRaw) : this(msgDataRaw.body)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GrmMsgData

        if (!body.contentEquals(other.body)) return false

        return true
    }

    override fun hashCode(): Int {
        return body.contentHashCode()
    }
}