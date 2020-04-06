package dsx.bps.core.datamodel

import java.math.BigDecimal

data class Payment(
    val id: String,
    val currency: Currency,
    val amount: BigDecimal,
    val address: String,
    val tag: String? = null
) {

    var status: PaymentStatus = PaymentStatus.PENDING
    lateinit var txid: TxId
    lateinit var fee: BigDecimal
}