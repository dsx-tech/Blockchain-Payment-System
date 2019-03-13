package dsx.bps.core.datamodel

import java.math.BigDecimal
import dsx.bps.core.Currency

interface Tx {

    fun currency(): Currency

    fun destination(): String

    fun amount(): BigDecimal

    fun fee(): BigDecimal

    fun confirmations(): Int

    fun hash(): String

    fun tag(): String
}