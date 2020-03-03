package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi

class GrmRawMessage(
    val source: String,
    val destination: String,
    val value: Long,
    val fwdFee: Long,
    val ihrFee: Long,
    val createdLt: Long,
    val bodyHash: ByteArray,
    val msgData: GrmMsgData
) {

    constructor(rawMessage: TonApi.RawMessage) : this(
        rawMessage.source, rawMessage.destination,
        rawMessage.value, rawMessage.fwdFee, rawMessage.ihrFee,
        rawMessage.createdLt, rawMessage.bodyHash,
        GrmMsgData(rawMessage.msgData as TonApi.MsgDataRaw)
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GrmRawMessage

        if (source != other.source) return false
        if (destination != other.destination) return false
        if (value != other.value) return false
        if (fwdFee != other.fwdFee) return false
        if (ihrFee != other.ihrFee) return false
        if (createdLt != other.createdLt) return false
        if (!bodyHash.contentEquals(other.bodyHash)) return false
        if (msgData != other.msgData) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + destination.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + fwdFee.hashCode()
        result = 31 * result + ihrFee.hashCode()
        result = 31 * result + createdLt.hashCode()
        result = 31 * result + bodyHash.contentHashCode()
        result = 31 * result + msgData.hashCode()
        return result
    }
}