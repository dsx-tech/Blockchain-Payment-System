package dsx.bps.kotlin.core

import java.math.BigDecimal
import java.util.*

data class Payment(val currency: Currency, val outputs: Map<String, BigDecimal>) {
    val id: UUID = UUID.randomUUID()
    val txIds: MutableList<String> = mutableListOf()

    constructor(currency: Currency, outputs: Map<String, BigDecimal>, txId: String): this(currency, outputs) {
        txIds.add(txId)
    }

    constructor(currency: Currency, outputs: Map<String, BigDecimal>, txIds: Iterable<String>): this(currency, outputs) {
        this.txIds.addAll(txIds)
    }
}