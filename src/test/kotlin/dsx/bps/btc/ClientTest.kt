package dsx.bps.btc

import java.math.BigDecimal
import org.junit.jupiter.api.Test
import dsx.bps.core.CoinClient

internal abstract class ClientTest {

    abstract val alice: CoinClient
    abstract val bob: CoinClient
    abstract val carol: CoinClient
    abstract val addresses: List<String>

    @Test
    open fun sendInvoice() {
    }

    @Test
    fun getInvoice() {
    }

    @Test
    fun getInvoice1() {
    }

    @Test
    fun sendPaymentToAddress() {
        val balance = alice.getBalance()
        val amount = balance / BigDecimal(10)
        val txid = alice.sendPayment(addresses[0], amount)
        val newBalance = alice.getBalance()

        assert(balance - amount >= newBalance)
        println("old balance: $balance, new balance: $newBalance, txid: $txid")
    }

    @Test
    fun sendPaymentToAddresses() {
        val balance = alice.getBalance()
        val numRecipients = BigDecimal(addresses.size)
        val amount = balance / BigDecimal(10) / numRecipients
        val outs = addresses.associate { address -> address to amount }
        val txid = alice.sendPayment(outs)
        val newBalance = alice.getBalance()
        assert(balance - (amount * numRecipients) >= newBalance)
        println("old balance: $balance, new balance: $newBalance, txid: $txid")
    }
}