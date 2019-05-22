package dsx.bps.crypto

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.crypto.common.Explorer
import dsx.bps.crypto.common.ExplorerFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import java.util.*

class Explorers(coins: Coins, private val config: Properties) {

    private val explorers: Map<Currency, Explorer>
    private val emitter: Observable<Tx>

    init {
        explorers = coins
            .coins
            .mapValues { ExplorerFactory.create(it.value, config) }

        emitter = Observable
            .merge(explorers.map { it.value.emitter })
            .observeOn(Schedulers.computation())
    }

    fun subscribe(observer: Observer<Tx>) {
        emitter.subscribe(observer)
    }

    fun subscribeOnCoin(currency: Currency, observer: Observer<Tx>) {
        explorers[currency]?.subscribe(observer)
            ?: throw Exception("Currency ${currency.name} isn't specified in configuration file.")
    }
}