package dsx.bps.crypto.btc.datamodel

import java.math.BigDecimal
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx

data class BtcTxSinceBlock(
    val address: String,
    val category: String,
    val amount: BigDecimal,
    val fee: BigDecimal,
    val confirmations: Int,
    val txid: String,
    val vout: Int
): Tx {

    override fun currency() = Currency.BTC

    override fun hash() = txid

    override fun index() = vout

    override fun amount() = amount

    override fun destination() = address

    override fun fee() = fee

    override fun confirmations() = confirmations
}