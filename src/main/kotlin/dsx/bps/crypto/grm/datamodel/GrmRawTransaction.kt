package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi


data class GrmRawTransaction(
        val utime: Long,
        val data: String,
        val transactionId: GrmInternalTxId,
        val fee: Long,
        val storageFee: Long,
        val otherFee: Long,
        val inMsg: GrmRawMessage,
        val outMsg: Array<GrmRawMessage>
) {

    constructor(rawTransaction: TonApi.RawTransaction) : this(
            rawTransaction.utime,
            byteArrayToHex(rawTransaction.data),
            GrmInternalTxId(rawTransaction.transactionId),
            rawTransaction.fee,
            rawTransaction.storageFee,
            rawTransaction.otherFee,
            GrmRawMessage(rawTransaction.inMsg),
            rawTransaction.outMsgs.map {GrmRawMessage(it)}.toTypedArray()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GrmRawTransaction

        if (utime != other.utime) return false
        if (data != other.data) return false
        if (transactionId != other.transactionId) return false
        if (fee != other.fee) return false
        if (storageFee != other.storageFee) return false
        if (otherFee != other.otherFee) return false
        if (inMsg != other.inMsg) return false
        if (!outMsg.contentEquals(other.outMsg)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = utime.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + transactionId.hashCode()
        result = 31 * result + fee.hashCode()
        result = 31 * result + storageFee.hashCode()
        result = 31 * result + otherFee.hashCode()
        result = 31 * result + inMsg.hashCode()
        result = 31 * result + outMsg.contentHashCode()
        return result
    }
}