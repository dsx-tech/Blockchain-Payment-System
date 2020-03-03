package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi

class GrmRawTransactions {

    val transactions: Array<GrmRawTransaction>
    val previousTransactionId: GrmInternalTxId

    constructor(
        txs: Array<GrmRawTransaction>,
        previousTxId: GrmInternalTxId
    ) {
        transactions = txs
        previousTransactionId = previousTxId
    }

    constructor(rawTransactions: TonApi.RawTransactions) {
        val grmTxs: ArrayList<GrmRawTransaction> = ArrayList()
        rawTransactions.transactions.forEach {
            grmTxs.add(GrmRawTransaction(it))
        }
        transactions = grmTxs.toTypedArray()
        previousTransactionId = GrmInternalTxId(
            rawTransactions.previousTransactionId
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GrmRawTransactions

        if (!transactions.contentEquals(other.transactions)) return false
        if (previousTransactionId != other.previousTransactionId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = transactions.contentHashCode()
        result = 31 * result + previousTransactionId.hashCode()
        return result
    }
}