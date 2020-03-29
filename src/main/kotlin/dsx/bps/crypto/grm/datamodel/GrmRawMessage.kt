package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi
import dsx.bps.exception.crypto.grm.GrmException

class GrmRawMessage {

    val source: String
    val destination: String
    val value: Long
    val fwdFee: Long
    val ihrFee: Long
    val createdLt: Long
    val bodyHash: String
    val msgText: String


    constructor(
        source: String, destination: String, value: Long, fwdFee: Long,
        ihrFee: Long, createdLt: Long, bodyHash: String, msgText: String
    ) {
        this.source = source
        this.destination = destination
        this.value = value
        this.fwdFee = fwdFee
        this.ihrFee = ihrFee
        this.createdLt = createdLt
        this.bodyHash = bodyHash
        this.msgText = msgText
    }

    constructor(rawMessage: TonApi.RawMessage) : this(
        rawMessage.source.accountAddress, rawMessage.destination.accountAddress,
        rawMessage.value, rawMessage.fwdFee, rawMessage.ihrFee,
        rawMessage.createdLt,
        byteArrayToHex(rawMessage.bodyHash),

        when (rawMessage.msgData) {
            is TonApi.MsgDataRaw -> bocDataToText((rawMessage.msgData as TonApi.MsgDataRaw).body)
            is TonApi.MsgDataText -> (rawMessage.msgData as TonApi.MsgDataText).text.toString(Charsets.UTF_8)
            else -> throw GrmException(
                "Can not cast RawMessage.msgData in $rawMessage to TonApi.MsgDataRaw or TonApi.MsgDataText"
            )
        }
    )
}