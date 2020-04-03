package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi

data class GrmQueryFees(
    val sourceFees: GrmFees,
    val destinationFees: Array<GrmFees>
) {
    constructor(queryFees: TonApi.QueryFees) : this(
        GrmFees(queryFees.sourceFees),
        queryFees.destinationFees.map {
            GrmFees(it)
        }.toTypedArray()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GrmQueryFees

        if (sourceFees != other.sourceFees) return false
        if (!destinationFees.contentEquals(other.destinationFees)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sourceFees.hashCode()
        result = 31 * result + destinationFees.contentHashCode()
        return result
    }
}