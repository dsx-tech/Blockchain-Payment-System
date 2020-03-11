package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi

data class GrmRawMessage(
    val source: String,
    val destination: String,
    val value: Long,
    val fwdFee: Long,
    val ihrFee: Long,
    val createdLt: Long,
    val bodyHash: String,
    val msgData: GrmMsgData
) {

    constructor(rawMessage: TonApi.RawMessage) : this(
        rawMessage.source, rawMessage.destination,
        rawMessage.value, rawMessage.fwdFee, rawMessage.ihrFee,
        rawMessage.createdLt,
        byteArrayToHex(rawMessage.bodyHash),
        //TODO: Транзакции могут содержать как MsgDataRaw (который нужно как-то парсить)
        // так и MsgDataText (например перевод средств от testgiver'a), который содержит готовое значение.
        // Падает тут если кастить не к тому классу.
        GrmMsgData(rawMessage.msgData as TonApi.MsgDataRaw)
    )
}