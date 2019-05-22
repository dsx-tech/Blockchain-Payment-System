package dsx.bps.crypto.common

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.rpc.JsonRpcHttpClient
import java.math.BigDecimal
import java.util.*

abstract class Coin(protected val config: Properties) {

    abstract val currency: Currency

    protected abstract val rpc: JsonRpcHttpClient

    abstract val address: String

    abstract val balance: BigDecimal

    open fun tag(): Int? = null

    abstract fun tx(txid: TxId): Tx

    abstract fun send(amount: BigDecimal, destination: String, tag: Int? = null): Tx
}