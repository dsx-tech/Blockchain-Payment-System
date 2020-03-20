package dsx.bps.api

import dsx.bps.TestUtils
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.crypto.btc.BtcRpc
import dsx.bps.crypto.eth.KFixedHostPortGenericContainer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Testcontainers
internal class BlockchainPaymentSystemAPITestBTC {

    private val aliceConfigPath = (TestUtils.getResourcePath("AliceConfigBTC.yaml"))
    private val bobConfigPath = (TestUtils.getResourcePath("BobConfigBTC.yaml"))

    private lateinit var aliceAPI : BlockchainPaymentSystemAPI
    private lateinit var bobAPI : BlockchainPaymentSystemAPI

    private lateinit var generator : BtcRpc

    private val aliceBtcAddress = "2MtYy1RGY2msh9WbRBf5VwUAE4xtGNJ9GQc"
    private val bobBtcAddress = "2NETNm86ug9drkCJ7N4U5crA9B9681HidzX"

    companion object {
        @Container
        @JvmStatic
        val container: KFixedHostPortGenericContainer = KFixedHostPortGenericContainer("siandreev/bitcoind-regtest:alice-bob-regtest")
            .withFixedExposedPort(18444, 18444)
            .withFixedExposedPort(18443, 18443)
            .waitingFor(
                Wait.forLogMessage(".*The node is ready!.*", 1))
    }

    @BeforeAll
    fun setUp() {
        val address = container.containerIpAddress
        generator = BtcRpc("http://bob:password@$address:18444/")

        aliceAPI = BlockchainPaymentSystemAPI(aliceConfigPath)
        bobAPI = BlockchainPaymentSystemAPI(bobConfigPath)
    }

    @Order(1)
    @Test
    fun getBalance() {
        assertDoesNotThrow {
           assertEquals(aliceAPI.getBalance(Currency.BTC), "5050.0")
           assertEquals(bobAPI.getBalance(Currency.BTC),"50.0")
        }
    }

    @Order(2)
    @Test
    fun sendPayment() {
        assertDoesNotThrow {
            val id1 = aliceAPI.sendPayment(Currency.BTC, 5.05, bobBtcAddress)
            Thread.sleep(1000)

            val id2 = bobAPI.sendPayment(Currency.BTC, 2.52, aliceBtcAddress)
            Thread.sleep(1000)

            val pay1 = aliceAPI.getPayment(id1)
            val pay2 = bobAPI.getPayment(id2)
            assertNotNull(pay1)
            assertNotNull(pay2)

            var count = 0
            while (pay1!!.status != PaymentStatus.SUCCEED ||
                pay2!!.status != PaymentStatus.SUCCEED) {
                count += 1
                generator.generatetoaddress(1, bobBtcAddress)
                Thread.sleep(2000)
                assertNotEquals(10, count, "Payment wasn't confirmed or found in 10 blocks")
            }
        }
    }

    @Disabled
    @Order(3)
    @Test
    fun createInvoice() {
        val invId = aliceAPI.createInvoice(Currency.BTC, 0.42)
        val inv = aliceAPI.getInvoice(invId)
        assertNotNull(inv)

        val id1 = bobAPI.sendPayment(inv!!.currency, inv.amount, inv.address)
        val pay1 = bobAPI.getPayment(id1)
        println(pay1)
        Thread.sleep(2000)

        var count = 0
        while (inv.status != InvoiceStatus.PAID) {
            count += 1
            generator.generatetoaddress(1, bobBtcAddress)
            Thread.sleep(3000)
            assertNotEquals(10, count, "Invoice wasn't paid or found in 10 blocks")
        }
    }

    @Disabled
    @Order(4)
    @Test
    fun createInvoiceWithTwoPayments() {
        val invId = aliceAPI.createInvoice(Currency.BTC, 10.2)
        val inv = aliceAPI.getInvoice(invId)
        assertNotNull(inv)

        val half = inv!!.amount / BigDecimal(2)
        bobAPI.sendPayment(inv.currency, half, inv.address)
        Thread.sleep(2000)
        Thread.sleep(2000)

        bobAPI.sendPayment(inv.currency, half, inv.address)
        Thread.sleep(2000)
        var count = 0
        while (inv.status != InvoiceStatus.PAID) {
            count += 1
            generator.generatetoaddress(1, bobBtcAddress)
            Thread.sleep(1000)
            assertNotEquals(10, count, "Invoice wasn't paid or found in 10 blocks")
        }
      }

}