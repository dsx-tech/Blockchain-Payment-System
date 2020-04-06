package dsx.bps.crypto.common

import com.uchuhimo.konf.Config
import dsx.bps.connection.Connector
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import io.reactivex.subjects.PublishSubject
import java.math.BigDecimal

abstract class Coin {

    abstract val currency: Currency
    abstract val config: Config

    protected abstract val connector: Connector
    protected abstract val explorer: Explorer

    fun getTxEmitter(): PublishSubject<Tx> = explorer.emitter

    abstract fun getBalance(): BigDecimal

    abstract fun getAddress(): String

    open fun getTag(): String? = null

    abstract fun getTx(txid: TxId): Tx

    abstract fun sendPayment(amount: BigDecimal, address: String, tag: Int? = null): Tx

    @Deprecated("only for tests")
    open fun kill(){

    }
}