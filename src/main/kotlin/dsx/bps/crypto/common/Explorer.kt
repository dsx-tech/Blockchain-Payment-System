package dsx.bps.crypto.common

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import io.reactivex.subjects.PublishSubject

abstract class Explorer(protected var frequency: Long) {

    protected abstract val coin: Coin
    protected abstract val currency: Currency
    protected val viewed: HashSet<String> = hashSetOf()
    val emitter: PublishSubject<Tx> = PublishSubject.create()

    protected abstract fun explore()
}