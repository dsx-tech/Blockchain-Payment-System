package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi

data class GrmFees(
    val inFwdFee: Long,
    val storageFee: Long,
    val gasFee: Long,
    val fwdFee: Long
) {
    constructor(fees: TonApi.Fees) : this(
        fees.inFwdFee, fees.storageFee, fees.gasFee, fees.fwdFee
    )
}