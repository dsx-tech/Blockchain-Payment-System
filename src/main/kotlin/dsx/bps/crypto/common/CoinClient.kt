package dsx.bps.crypto.common

import java.util.*
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import io.reactivex.subjects.PublishSubject
import dsx.bps.core.datamodel.Payment
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.rpc.JsonRpcHttpClient

abstract class CoinClient {

    abstract val currency: Currency
    protected val config: Properties

    protected abstract val rpc: JsonRpcHttpClient
    protected abstract val blockchainListener: BlockchainListener

    constructor() {
        config = Properties()
    }

    constructor(conf: Properties) {
        config = Properties(conf)
    }

    constructor(confPath: String): this() {
        val f = File(confPath)
        if (f.exists()) {
            FileInputStream(f).use { config.load(it) }
        }
    }

    fun getTxEmitter(): PublishSubject<Tx> = blockchainListener.emitter

    abstract fun getBalance(): BigDecimal

    abstract fun getAddress(): String

    abstract fun getTag(): Int?

    abstract fun getTx(txid: TxId): Tx

    fun getTxs(txids: List<TxId>): List<Tx> = txids.map(::getTx)

    abstract fun sendPayment(amount: BigDecimal, address: String, tag: Int? = null): Tx
}