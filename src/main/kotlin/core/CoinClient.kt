package dsx.bps.kotlin.core

import java.math.BigDecimal
import java.util.*

interface CoinClient {

    val symbol: String
    val currency: Currency

    fun sendInvoice(amount: BigDecimal): Invoice

    fun getInvoice(id: String): Invoice?

    fun getInvoice(uuid: UUID): Invoice?

    fun sendPayment(address: String, amount: BigDecimal): String

    fun sendPayment(outputs: Map<String, BigDecimal>): String

    fun getNewAddress(): String

    fun getBalance(): BigDecimal
}