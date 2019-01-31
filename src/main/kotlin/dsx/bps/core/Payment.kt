package dsx.bps.core

import java.util.UUID
import java.math.BigDecimal

data class Payment(val currency: Currency, val outputs: Map<String, BigDecimal>) {
    val id: String = UUID.randomUUID().toString()
    val txIds: MutableList<String> = mutableListOf()

    constructor(currency: Currency, outputs: Map<String, BigDecimal>, txId: String): this(currency, outputs) {
        txIds.add(txId)
    }

    constructor(currency: Currency, outputs: Map<String, BigDecimal>, txIds: Iterable<String>): this(currency, outputs) {
        this.txIds.addAll(txIds)
    }
}