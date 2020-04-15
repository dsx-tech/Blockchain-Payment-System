package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi
import dsx.bps.exception.crypto.grm.GrmException

data class GrmRawMessage(
        val source: String,
        val destination: String,
        val value: Long,
        val fwdFee: Long,
        val ihrFee: Long,
        val createdLt: Long,
        val bodyHash: String,
        val msgText: String
) {

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