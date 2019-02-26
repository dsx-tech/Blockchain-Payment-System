package dsx.bps.crypto.common

import java.util.*
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import io.reactivex.subjects.PublishSubject
import dsx.bps.core.Payment
import dsx.bps.core.Currency

abstract class CoinClient {

    abstract val currency: Currency
    protected val config: Properties

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

    // TODO: implement storage for payments
    protected val payments: HashMap<String, Payment> = HashMap()

    protected abstract val blockchainListener: BlockchainListener

    fun getTxEmitter(): PublishSubject<Tx> = blockchainListener.emitter

    abstract fun sendPayment(amount: BigDecimal, address: String): Payment

    abstract fun getAddress(): String

    abstract fun getBalance(): BigDecimal
}