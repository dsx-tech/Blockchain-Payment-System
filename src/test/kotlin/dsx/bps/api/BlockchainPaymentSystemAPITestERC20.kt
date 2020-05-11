package dsx.bps.api

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.crypto.eth.CommonConnector
import dsx.bps.crypto.eth.KFixedHostPortGenericContainer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.math.BigDecimal
import java.nio.file.Files


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Testcontainers
internal class BlockchainPaymentSystemAPITestERC20 {

    private val aliceConfigPath = "./src/test/resources/AliceConfigETH.yaml"
    private val bobConfigPath = "./src/test/resources/BobConfigETH.yaml"

    private lateinit var aliceAPI: BlockchainPaymentSystemAPI
    private lateinit var bobAPI: BlockchainPaymentSystemAPI

    private val aliceEthAddress = "0x073cfa4b6635b1a1b96f6363a9e499a8076b6107"
    private val bobEthAddress = "0x0ce59225bcd447feaed698ed754d309feba5fc63"

    private lateinit var generator: CommonConnector

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
        Thread.sleep(4000)
        aliceAPI = BlockchainPaymentSystemAPI(aliceConfigPath)
        bobAPI = BlockchainPaymentSystemAPI(bobConfigPath)

        val address = container.containerIpAddress
        val url = "http://$address:8542"

        generator = CommonConnector(url)
    }

    @AfterAll
    fun tearDown(){
        //aliceAPI.kill(Currency.ETH)
        //bobAPI.kill(Currency.ETH)
      //  removeNewWallets()
    }

    @Order(1)
    @Test
    fun getBalance() {
        assertDoesNotThrow {
            val realBalance = BigDecimal.valueOf(1000)
            val expected = BigDecimal(aliceAPI.getBalance(Currency.USDT))
            val result = realBalance.compareTo(expected)
            assertEquals(result, 0)
        }
    }

    @Order(2)
    @Test
    fun sendPayments() {
        // send first payment
        assertDoesNotThrow {
            aliceAPI.clearDb(Currency.USDT)

            val id1 = aliceAPI.sendPayment(Currency.USDT, 10.0, bobEthAddress)
            Thread.sleep(1000)
            waitForSomeBlocksMining()
            Thread.sleep(1000)
            val id2 = bobAPI.sendPayment(Currency.USDT, 7.0, aliceEthAddress)
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
                assertNotEquals(10, count, "Payment wasn't confirmed or found in >= 5 blocks")
            }
        }

        // send second payment
        assertDoesNotThrow {
            val id1 = aliceAPI.sendPayment(Currency.USDT, 30, bobEthAddress)
            Thread.sleep(1000)
            waitForSomeBlocksMining()
            Thread.sleep(1000)
            val id2 = bobAPI.sendPayment(Currency.USDT, 12, aliceEthAddress)
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
                assertNotEquals(10, count, "Payment wasn't confirmed or found in >= 5 blocks")
            }
        }
    }

    @Disabled
    @Order(3)
    @Test
    fun createInvoice() {
        val aliceBalance = aliceAPI.getBalance(Currency.USDT)
        val invId = aliceAPI.createInvoice(Currency.USDT, 34.5)
        val inv = aliceAPI.getInvoice(invId)

        assertNotNull(inv)
        bobAPI.sendPayment(inv!!.currency, inv.amount, inv.address)

        var count1 = 0
        while (inv.status != InvoiceStatus.PAID) {
            waitForSomeBlocksMining()
            count1 += 1
            Thread.sleep(2000)
            assertNotEquals(6, count1, "Invoice wasn't paid or found in >= 6 blocks")
        }

        /*

       var count2 = 0
       while (aliceAPI.getBalance(Currency.ETH) == aliceBalance)
       {
           waitForSomeBlocksMining()
           count2 += 1
           Thread.sleep(2000)
           assertNotEquals(6, count2, "Money was not transferred to a hot wallet in >= 6 blocks")
       } */
    }

    @Disabled
    @Order(4)
    @Test
    fun createInvoiceWithTwoPayments() {
        val bobBalance = bobAPI.getBalance(Currency.ETH)
        val invId = bobAPI.createInvoice(Currency.ETH, 0.06)
        val inv = bobAPI.getInvoice(invId)

        assertNotNull(inv)

        val half = inv!!.amount / BigDecimal(2)
        aliceAPI.sendPayment(inv.currency, half, inv.address)
        waitForSomeBlocksMining()

        aliceAPI.sendPayment(inv.currency, half, inv.address)
        var count1 = 0
        while (inv.status != InvoiceStatus.PAID) {
            waitForSomeBlocksMining()
            count1 += 1
            Thread.sleep(3000)
            assertNotEquals(10, count1, "Invoice wasn't paid or found in >= 6 blocks")
        }

        var count2 = 0
        println("bob start balance $bobBalance")
        while ((bobAPI.getBalance(Currency.ETH).toDouble() - bobBalance.toDouble()) <= 0.05)
        {
            println("bob balance ${bobAPI.getBalance(Currency.ETH)}")
            waitForSomeBlocksMining()
            count2 += 1
            Thread.sleep(2000)
            assertNotEquals(6, count2, "Money was not transferred to a hot wallet in >= 6 blocks")
        }

    }

    private fun waitForSomeBlocksMining() {
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

    private fun removeNewWallets() {
        val bobDir = "./src/test/resources/ETH/bobWallet"
        val aliceDir = "./src/test/resources/ETH/aliceWallet"

        val fl_bob = File(bobDir)
        val files_bob = fl_bob.listFiles { file -> file.isFile }

        for (file in files_bob!!) {
            if (file.name != "UTC--2020-03-04T08-33-39.016502000Z--0ce59225bcd447feaed698ed754d309feba5fc63")
            {
                Files.delete(file.toPath())
            }
        }

        val fl_alice = File(aliceDir)
        val files_alice = fl_alice.listFiles { file -> file.isFile }

        for (file in files_alice!!) {
            if (file.name != "UTC--2020-03-04T08-33-21.065924100Z--073cfa4b6635b1a1b96f6363a9e499a8076b6107")
            {
                Files.delete(file.toPath())
            }
        }
    }
}