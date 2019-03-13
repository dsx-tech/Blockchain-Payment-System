package dsx.bps.crypto.common

import dsx.bps.core.datamodel.Tx
import io.reactivex.subjects.PublishSubject

interface BlockchainListener {

    var frequency: Long

    val viewed: HashSet<String>

    val emitter: PublishSubject<Tx>

    fun explore()
}