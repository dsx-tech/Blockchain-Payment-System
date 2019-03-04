package dsx.bps.crypto.btc.datamodel

import java.math.BigDecimal
import dsx.bps.core.Currency
import dsx.bps.crypto.common.Tx

data class BtcTxSinceBlock(
    val address: String,
    val category: String,
    val amount: BigDecimal,
    val fee: BigDecimal,
    val confirmations: Int,
    val txid: String
): Tx {

    override fun currency() = Currency.BTC

    override fun destination() = address

    override fun amount() = amount

    override fun fee() = fee

    override fun confirmations() = confirmations

    override fun hash() = txid

    override fun tag() = ""
}