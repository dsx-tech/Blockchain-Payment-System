package dsx.bps.crypto.common

import java.math.BigDecimal

interface Tx {

    fun destination(): String

    fun amount(): BigDecimal

    fun fee(): BigDecimal

    fun confirmations(): Int

    fun hash(): String

    fun timestamp(): Long

    fun tag(): String
}