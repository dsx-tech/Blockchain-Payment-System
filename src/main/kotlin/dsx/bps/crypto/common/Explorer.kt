package dsx.bps.crypto.common

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlin.collections.HashSet

abstract class Explorer(protected val config: Properties) {

    protected abstract val coin: Coin
    protected abstract val currency: Currency
    protected val viewed: HashSet<String> = hashSetOf()
    val emitter: PublishSubject<Tx> = PublishSubject.create()
    abstract var frequency: Long

    protected abstract fun explore()

    fun subscribe(observer: Observer<Tx>) {
        emitter.subscribe(observer)
    }
}