package dsx.bps.core.datamodel

import java.math.BigDecimal

interface Tx {

    fun currency(): Currency

    fun txid(): TxId = TxId(hash(), index())

    fun hash(): String

    fun index(): Long = 0

    fun amount(): BigDecimal

    fun destination(): String

    fun paymentReference(): String? = null

    fun fee(): BigDecimal

    fun status(): TxStatus
}