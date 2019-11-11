package dsx.bps.crypto.common

import com.uchuhimo.konf.Config
import java.math.BigDecimal
import io.reactivex.subjects.PublishSubject
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.rpc.JsonRpcHttpClient

abstract class CoinClient {

    abstract val currency: Currency
    abstract val config: Config

    protected abstract val rpc: JsonRpcHttpClient
    protected abstract val blockchainListener: BlockchainListener

    fun getTxEmitter(): PublishSubject<Tx> = blockchainListener.emitter

    abstract fun getBalance(): BigDecimal

    abstract fun getAddress(): String

    abstract fun getTag(): Int?

    abstract fun getTx(txid: TxId): Tx

    fun getTxs(txids: List<TxId>): List<Tx> = txids.map(::getTx)

    abstract fun sendPayment(amount: BigDecimal, address: String, tag: Int? = null): Tx
}