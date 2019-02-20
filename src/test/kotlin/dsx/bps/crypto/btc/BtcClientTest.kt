package dsx.bps.crypto.btc

import java.math.BigDecimal
import dsx.bps.core.InvoiceStatus
import dsx.bps.crypto.ClientTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BtcClientTest: ClientTest() {

    private val configDir = System.getProperty("user.home") + File.separator + "bps" + File.separator
    private val aliceConfigPath = configDir + "alice.properties"
    private val bobConfigPath = configDir + "bob.properties"
    private val carolConfigPath = configDir + "carol.properties"

    override val alice = BtcClient(aliceConfigPath)
    override val carol = BtcClient(carolConfigPath)
    override val bob = BtcClient(bobConfigPath)

    override val addresses = listOf(
        bob.getNewAddress(),
        carol.getNewAddress()
    )

    @Test
    override fun createInvoice() {
        val aliceBalance = alice.getBalance()
        val amount = aliceBalance / BigDecimal(10)
        val inv = bob.createInvoice(amount)
        alice.sendPayment(inv.amount, inv.address)

        Thread.sleep(1000)
        alice.rpc.generate(1)
        Thread.sleep(2000)
        bob.rpc.generate(1)
        Thread.sleep(2000)
        carol.rpc.generate(1)
        Thread.sleep(2000)

        assertEquals(InvoiceStatus.PAID, inv.status)
    }
}