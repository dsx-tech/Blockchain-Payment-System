package dsx.bps.crypto.common

import dsx.bps.core.datamodel.Tx
import io.reactivex.subjects.PublishSubject

abstract class BlockchainListener(protected val frequency: Long) {

    protected abstract val coin: CoinClient
    protected val viewed: HashSet<String> = hashSetOf()
    val emitter: PublishSubject<Tx> = PublishSubject.create()

    abstract fun explore()
}