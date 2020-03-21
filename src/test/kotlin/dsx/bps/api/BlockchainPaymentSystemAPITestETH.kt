package dsx.bps.api

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.crypto.eth.EthRpc
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
internal class BlockchainPaymentSystemAPITestETH {

    private val aliceConfigPath = "./src/test/resources/AliceConfigETH.yaml"
    private val bobConfigPath = "./src/test/resources/BobConfigETH.yaml"

    private lateinit var aliceAPI: BlockchainPaymentSystemAPI
    private lateinit var bobAPI: BlockchainPaymentSystemAPI

    private val aliceEthAddress = "0x073cfa4b6635b1a1b96f6363a9e499a8076b6107"
    private val bobEthAddress = "0x0ce59225bcd447feaed698ed754d309feba5fc63"

    private lateinit var generator: EthRpc

    companion object {
        @Container
        @JvmStatic
        val container = KFixedHostPortGenericContainer("siandreev/ethereum-rpc-test:PoA-mining")
            .withFixedExposedPort(8541, 8541)
            .withFixedExposedPort(8542, 8542)
            .waitingFor(
                Wait.forLogMessage(".*The node is ready!.*", 1)
            )
    }

    @BeforeAll
    fun setUp() {
        aliceAPI = BlockchainPaymentSystemAPI(aliceConfigPath)
        bobAPI = BlockchainPaymentSystemAPI(bobConfigPath)

        val address = container.containerIpAddress
        val url = "http://$address:8541"
        generator = EthRpc(url)
    }

    @AfterAll
    fun tearDown(){
        aliceAPI.kill(Currency.ETH)
        bobAPI.kill(Currency.ETH)
    }

    @Order(1)
    @Test
    fun getBalance() {
        assertDoesNotThrow {
            val realBalance = "50"
            assertEquals(realBalance, aliceAPI.getBalance(Currency.ETH))
        }
    }

    @Order(2)
    @Test
    fun sendPayment() {
        assertDoesNotThrow {
            val id1 = aliceAPI.sendPayment(Currency.ETH, 1.0, bobEthAddress)
            Thread.sleep(1000)
            waitForSomeBlocksMining()
            Thread.sleep(1000)
            val id2 = bobAPI.sendPayment(Currency.ETH, 0.2, aliceEthAddress)
            waitForSomeBlocksMining()

            val pay1 = aliceAPI.getPayment(id1)
            val pay2 = bobAPI.getPayment(id2)
            assertNotNull(pay1)
            assertNotNull(pay2)

            var count = 0
            while (pay1!!.status != PaymentStatus.SUCCEED ||
                pay2!!.status != PaymentStatus.SUCCEED) {
                count += 1
                waitForSomeBlocksMining()
                assertNotEquals(5, count, "Payment wasn't confirmed or found in >= 5 blocks")
            }
        }
    }

    @Disabled
    @Order(3)
    @Test
    fun createInvoice() {
        val invId = bobAPI.createInvoice(Currency.ETH, 0.03)
        val inv = bobAPI.getInvoice(invId)

        assertNotNull(inv)
        aliceAPI.sendPayment(inv!!.currency, inv.amount, inv.address)
        var count = 0
        while (inv.status != InvoiceStatus.PAID) {
            waitForSomeBlocksMining()
            count += 1
            assertNotEquals(10, count, "Invoice wasn't paid or found in >= 10 blocks")
        }
    }

    @Disabled
    @Order(4)
    @Test
    fun createInvoiceWithTwoPayments() {
        val invId = bobAPI.createInvoice(Currency.ETH, 0.06)
        val inv = bobAPI.getInvoice(invId)
        assertNotNull(inv)

        val half = inv!!.amount / BigDecimal(2)
        aliceAPI.sendPayment(inv.currency, half, inv.address)
        waitForSomeBlocksMining()

        aliceAPI.sendPayment(inv.currency, half, inv.address)
        var count = 0
        while (inv.status != InvoiceStatus.PAID) {
            waitForSomeBlocksMining()
            count += 1
            Thread.sleep(1000)
            assertNotEquals(10, count, "Invoice wasn't paid or found in >= 10 blocks")
        }
    }

    fun waitForSomeBlocksMining() {
        val latestHash = generator.getLatestBlock()
        var count = 0
        while (generator.getLatestBlock() == latestHash && count < 160) {
            Thread.sleep(5000)
            count++
        }
        if (count >= 160) {
            throw Exception("Block mining timed out")
        }
    }

}