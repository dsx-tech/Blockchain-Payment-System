package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi

data class GrmMsgData(val body: String) {

    constructor(msgDataRaw: TonApi.MsgDataRaw) : this(byteArrayToHex(msgDataRaw.body))
}