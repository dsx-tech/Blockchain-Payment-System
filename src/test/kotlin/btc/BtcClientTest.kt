package btc

import dsx.bps.kotlin.btc.BtcClient
import dsx.bps.kotlin.core.InvoiceStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BtcClientTest: ClientTest() {

    private val aliceURL = "http://alice:password@localhost:8333/"
    private val bobURL = "http://bob:password@localhost:8444/"
    private val carolURL = "http://carol:password@localhost:8555/"

    override val alice = BtcClient(aliceURL)
    override val carol = BtcClient(carolURL)
    override val bob = BtcClient(bobURL)

    override val addresses = listOf(
        alice.getNewAddress(),
        bob.getNewAddress(),
        carol.getNewAddress()
    )

    @BeforeAll
    @Test
    fun testSetup() {
        testConnect()
        testGenerate()
    }

    private fun testConnect() {
        assertDoesNotThrow {
            bob.connect("127.0.0.1:18333")
            carol.connect("127.0.0.1:18333")
        }

        Thread.sleep(1000)
        assertEquals(2, alice.rpc.connectionCount)
        assertEquals(1, bob.rpc.connectionCount)
        assertEquals(1, carol.rpc.connectionCount)
    }

    private fun testGenerate() {
        val amount = 50
        val blocksHashes: MutableList<String> = mutableListOf()
        assertDoesNotThrow {
            Thread.sleep(1000)
            blocksHashes.addAll(alice.rpc.generate(amount))
            Thread.sleep(1000)
            blocksHashes.addAll(bob.rpc.generate(amount))
            Thread.sleep(1000)
            blocksHashes.addAll(carol.rpc.generate(amount))
        }

        Thread.sleep(2000)
        assertEquals(amount * 3, blocksHashes.size)
        println("nodes generated $amount blocks")
    }

    @Test
    override fun sendInvoice() {
        val aliceBalance = alice.getBalance()
        val bobPrevBalance = bob.getBalance()
        val amount = aliceBalance / BigDecimal(10)
        val inv = bob.sendInvoice(amount)
        alice.sendPayment(inv.address, inv.amount)

        Thread.sleep(1000)
        alice.rpc.generate(1)
        Thread.sleep(2000)
        bob.rpc.generate(1)
        Thread.sleep(2000)
        carol.rpc.generate(1)
        Thread.sleep(2000)

        assertEquals(InvoiceStatus.PAID, inv.status)

        val bobNewBalance = bob.getBalance()
        assert(bobPrevBalance + inv.amount >= bobNewBalance)
    }
}