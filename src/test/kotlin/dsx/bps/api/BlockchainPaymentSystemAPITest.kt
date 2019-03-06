package dsx.bps.api

import dsx.bps.core.Currency
import dsx.bps.core.InvoiceStatus
import dsx.bps.crypto.btc.BtcClient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BlockchainPaymentSystemAPITest {

    private val configDir = System.getProperty("user.home") + File.separator + "bps" + File.separator
    private val aliceConfigPath = configDir + "alice.properties"
    private val bobConfigPath = configDir + "bob.properties"
    private val carolConfigPath = configDir + "carol.properties"

    private val aliceAPI = BlockchainPaymentSystemAPI(aliceConfigPath)
    private val bobAPI = BlockchainPaymentSystemAPI(bobConfigPath)
    private val generator = BtcClient(carolConfigPath).rpc

    private val aliceBtcAddress = "2MtYy1RGY2msh9WbRBf5VwUAE4xtGNJ9GQc"
    private val bobBtcAddress = "2NETNm86ug9drkCJ7N4U5crA9B9681HidzX"

    @Test
    @BeforeAll
    fun getBalance() {
        assertDoesNotThrow {
            aliceAPI.getBalance(Currency.BTC)
            bobAPI.getBalance(Currency.BTC)
        }
    }

    @Test
    fun sendPayment() {
        assertDoesNotThrow {
            aliceAPI.sendPayment(Currency.BTC, 50.05, bobBtcAddress)
            Thread.sleep(1000)
            generator.generate(1)
            Thread.sleep(1000)
            bobAPI.sendPayment(Currency.BTC, 25.52, aliceBtcAddress)
            generator.generate(1)
        }
    }

    @Test
    fun createInvoice() {
        val invId = aliceAPI.createInvoice(Currency.BTC, 0.42)
        val inv = aliceAPI.getInvoice(invId)

        assertNotNull(inv)
        bobAPI.sendPayment(inv!!.currency, inv.amount, inv.address)
        Thread.sleep(2000)

        assertTimeout(Duration.ofSeconds(10)) {
            while (inv.status != InvoiceStatus.PAID) {
                generator.generate(1)
                Thread.sleep(1000)
            }
        }
    }
}