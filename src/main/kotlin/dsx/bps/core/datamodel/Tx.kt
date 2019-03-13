package dsx.bps.core.datamodel

import java.math.BigDecimal

interface Tx {

    fun currency(): Currency

    fun hash(): String

    fun index(): Int

    fun amount(): BigDecimal

    fun destination(): String

    fun tag(): Int? = null

    fun fee(): BigDecimal

    fun confirmations(): Int
}