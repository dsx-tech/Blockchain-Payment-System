package btc

import dsx.bps.kotlin.core.CoinClient
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal abstract class ClientTest {

    abstract val client: CoinClient
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
        val balance = client.getBalance()
        val amount = balance / BigDecimal(10)
        val txid = client.sendPayment(addresses[0], amount)
        val newBalance = client.getBalance()

        assert(balance - amount >= newBalance)
        println("old balance: $balance, new balance: $newBalance, txid: $txid")
    }

    @Test
    fun sendPaymentToAddresses() {
        val balance = client.getBalance()
        val numRecipients = BigDecimal(addresses.size)
        val amount = balance / BigDecimal(10) / numRecipients
        val outs = addresses.associate { address -> address to amount }
        val txid = client.sendPayment(outs)
        val newBalance = client.getBalance()
        assert(balance - (amount * numRecipients) >= newBalance)
        println("old balance: $balance, new balance: $newBalance, txid: $txid")
    }
}