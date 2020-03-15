package dsx.bps.crypto.grm.datamodel

import dsx.bps.exception.crypto.grm.GrmException
import dsx.bps.ton.api.TonApi

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
        rawMessage.source, rawMessage.destination,
        rawMessage.value, rawMessage.fwdFee, rawMessage.ihrFee,
        rawMessage.createdLt,
        byteArrayToHex(rawMessage.bodyHash),
        //TODO: Транзакции могут содержать как MsgDataRaw (который нужно как-то парсить)
        // так и MsgDataText (например перевод средств от testgiver'a), который содержит готовое значение.
        // Падает тут если кастить не к тому классу.
        when (rawMessage.msgData) {
            is TonApi.MsgDataRaw -> convertDataToText((rawMessage.msgData as TonApi.MsgDataRaw).body)
            is TonApi.MsgDataText -> rawMessage.msgData.toString()
            else -> throw GrmException(
                "Can not cast RawMessage.msgData in $rawMessage to TonApi.MsgDataRaw or TonApi.MsgDataText"
            )
        }
    )
}