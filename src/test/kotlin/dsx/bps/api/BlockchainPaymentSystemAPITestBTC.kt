package dsx.bps.api

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.crypto.btc.BtcRpc
import dsx.bps.crypto.eth.KFixedHostPortGenericContainer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal

@Testcontainers
internal class BlockchainPaymentSystemAPITestBTC {

    private val aliceConfigPath = javaClass.classLoader.getResource("AliceConfigBTC.yaml")?.path
    private val bobConfigPath = javaClass.classLoader.getResource("BobConfigBTC.yaml")?.path

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
    }

    @BeforeEach
    fun setUp() {
        val address = container.containerIpAddress
        generator = BtcRpc("http://bob:password@$address:18444/")
        Thread.sleep(8000)

        aliceAPI = BlockchainPaymentSystemAPI(aliceConfigPath!!)
        bobAPI = BlockchainPaymentSystemAPI(bobConfigPath!!)
    }



    @Test
    fun getBalance() {
        assertDoesNotThrow {
            println("alice balance: ${aliceAPI.getBalance(Currency.BTC)}")
            println("bob balance: ${bobAPI.getBalance(Currency.BTC)}")
        }
    }

    @Test
    fun sendPayment() {
        assertDoesNotThrow {
            generator.generate(101)
            val id1 = aliceAPI.sendPayment(Currency.BTC, 50.05, bobBtcAddress)
            Thread.sleep(1000)

            val id2 = bobAPI.sendPayment(Currency.BTC, 20.52, aliceBtcAddress)
            Thread.sleep(1000)

            val pay1 = aliceAPI.getPayment(id1)
            val pay2 = bobAPI.getPayment(id2)
            assertNotNull(pay1)
            assertNotNull(pay2)
            println("alice's payment was sent in ${pay1!!.txid}")
            println("bob's payment was sent in ${pay2!!.txid}")
            var count = 0
            while (pay1.status != PaymentStatus.SUCCEED ||
                pay2.status != PaymentStatus.SUCCEED) {
                count += 1
                generator.generate(1)
                Thread.sleep(2000)
                println("alice's payment status: ${pay1.status}")
                println("bob's payment status: ${pay2.status}")
                assertNotEquals(10, count, "Payment wasn't confirmed or found in 10 blocks")
            }
        }
    }

    @Test
    fun createInvoice() {
        val invId = aliceAPI.createInvoice(Currency.BTC, 0.42)
        val inv = aliceAPI.getInvoice(invId)

        assertNotNull(inv)
        bobAPI.sendPayment(inv!!.currency, inv.amount, inv.address)
        Thread.sleep(2000)

        var count = 0
        while (inv.status != InvoiceStatus.PAID) {
            count += 1
            generator.generate(1)
            Thread.sleep(1000)
            assertNotEquals(10, count, "Invoice wasn't paid or found in 10 blocks")
        }
    }

    @Test
    fun createInvoiceWithTwoPayments() {
        val invId = aliceAPI.createInvoice(Currency.BTC, 100.2)
        val inv = aliceAPI.getInvoice(invId)
        assertNotNull(inv)

        val half = inv!!.amount / BigDecimal(2)
        bobAPI.sendPayment(inv.currency, half, inv.address)
        Thread.sleep(2000)
        Thread.sleep(2000)
        println("Received funds: ${inv.received} / ${inv.amount} in tx ${inv.txids}")

        bobAPI.sendPayment(inv.currency, half, inv.address)
        Thread.sleep(2000)
        var count = 0
        while (inv.status != InvoiceStatus.PAID) {
            count += 1
            generator.generate(1)
            Thread.sleep(1000)
            assertNotEquals(10, count, "Invoice wasn't paid or found in 10 blocks")
        }
        println("$inv :")
        println("   txs ${inv.txids}")
    }

}