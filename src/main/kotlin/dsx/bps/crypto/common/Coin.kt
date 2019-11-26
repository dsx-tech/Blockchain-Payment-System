package dsx.bps.crypto.common

import com.uchuhimo.konf.Config
import java.math.BigDecimal
import io.reactivex.subjects.PublishSubject
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.rpc.JsonRpcHttpClient

abstract class Coin {

    abstract val currency: Currency
    abstract val config: Config

    protected abstract val rpc: JsonRpcHttpClient
    protected abstract val explorer: Explorer

    fun getTxEmitter(): PublishSubject<Tx> = explorer.emitter

    abstract fun getBalance(): BigDecimal

    abstract fun getAddress(): String

    open fun getTag(): Int? = null

    abstract fun getTx(txid: TxId): Tx

    abstract fun sendPayment(amount: BigDecimal, address: String, tag: Int? = null): Tx
}