package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi


class GrmRawTransaction {

    val utime: Long
    val data: String
    val transactionId: GrmInternalTxId
    val fee: Long
    val storageFee: Long
    val otherFee: Long
    val inMsg: GrmRawMessage
    val outMsg: Array<GrmRawMessage>

    constructor(
        utime: Long, data: ByteArray, transactionId: GrmInternalTxId,
        fee: Long, storageFee: Long, otherFee: Long,
        inMsg: GrmRawMessage, outMsg: Array<GrmRawMessage>
    ) {
        this.utime = utime
        this.data = byteArrayToHex(data)
        this.transactionId = transactionId
        this.fee = fee
        this.storageFee = storageFee
        this.otherFee = otherFee
        this.inMsg = inMsg
        this.outMsg = outMsg
    }

    constructor(rawTransaction: TonApi.RawTransaction) {
        utime = rawTransaction.utime
        data = byteArrayToHex(rawTransaction.data)
        transactionId = GrmInternalTxId(rawTransaction.transactionId)
        fee = rawTransaction.fee
        storageFee = rawTransaction.storageFee
        otherFee = rawTransaction.otherFee
        inMsg = GrmRawMessage(rawTransaction.inMsg)

        val grmOutMsgList: ArrayList<GrmRawMessage> = ArrayList()
        rawTransaction.outMsgs.forEach {
            grmOutMsgList.add(GrmRawMessage(it))
        }
        outMsg = grmOutMsgList.toTypedArray()
    }

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

    override fun toString(): String {
        return "GrmRawTransaction(utime=$utime, data='$data'," +
                " transactionId=$transactionId, fee=$fee, storageFee=$storageFee," +
                " otherFee=$otherFee, inMsg=$inMsg, outMsg=${outMsg.contentToString()})"
    }
}