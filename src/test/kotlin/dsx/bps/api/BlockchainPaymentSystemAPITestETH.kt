package dsx.bps.api

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.crypto.eth.EthRpc
import dsx.bps.crypto.eth.KFixedHostPortGenericContainer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BlockchainPaymentSystemAPITestETH {

    private val aliceConfigPath = javaClass.classLoader.getResource("AliceConfigETH.yaml")?.path
    private val bobConfigPath = javaClass.classLoader.getResource("BobConfigETH.yaml")?.path

    private lateinit var aliceAPI: BlockchainPaymentSystemAPI
    private lateinit var bobAPI: BlockchainPaymentSystemAPI

    private val aliceEthAddress = "0xacfd9f1452e191fa39ff882e5fea428b999fb2af"
    private val bobEthAddress = "0x940c955f4072201fd9732bb5000c2d66dec449b6"

    private lateinit var generator: EthRpc

    companion object {
        @Container
        @JvmStatic
        val container = KFixedHostPortGenericContainer("siandreev/ethereum-rpc-test:mining")
            .withFixedExposedPort(8545, 8545)
    }

    @BeforeEach
    fun setUp() {
        aliceAPI = BlockchainPaymentSystemAPI(aliceConfigPath!!)
        bobAPI = BlockchainPaymentSystemAPI(bobConfigPath!!)

        val address = container.containerIpAddress
        val url = "http://$address:8545"
        generator = EthRpc(url)
    }

    @Disabled
    @Test
    fun getBalance() {
        assertDoesNotThrow {
            assertNotEquals(aliceAPI.getBalance(Currency.ETH), "0")
            //TODO: create another etherbase account
        }
    }

    @Disabled
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