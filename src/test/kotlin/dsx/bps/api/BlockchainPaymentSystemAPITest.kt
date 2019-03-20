package dsx.bps.api

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.crypto.btc.BtcRpc
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BlockchainPaymentSystemAPITest {

    private val configDir = System.getProperty("user.home") + File.separator + "bps" + File.separator
    private val aliceConfigPath = configDir + "alice.properties"
    private val bobConfigPath = configDir + "bob.properties"

    private val aliceAPI = BlockchainPaymentSystemAPI(aliceConfigPath)
    private val bobAPI = BlockchainPaymentSystemAPI(bobConfigPath)
    private val generator = BtcRpc("http://carol:password@34.253.178.165:8555/")

    private val aliceBtcAddress = "2MtYy1RGY2msh9WbRBf5VwUAE4xtGNJ9GQc"
    private val bobBtcAddress = "2NETNm86ug9drkCJ7N4U5crA9B9681HidzX"

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
            val id1 = aliceAPI.sendPayment(Currency.BTC, 50.05, bobBtcAddress)
            Thread.sleep(1000)
            generator.generate(1)
            Thread.sleep(1000)
            val id2 = bobAPI.sendPayment(Currency.BTC, 25.52, aliceBtcAddress)
            generator.generate(1)

            val pay1 = aliceAPI.getPayment(id1)
            val pay2 = bobAPI.getPayment(id2)
            assertNotNull(pay1)
            assertNotNull(pay2)
            println("alice's payment was sent in ${pay1!!.txid}")
            println("bob's payment was sent in ${pay2!!.txid}")
            var count = 0
            while (pay1.status != PaymentStatus.SUCCEED ||
                    pay2.status != PaymentStatus.SUCCEED) {
                generator.generate(1)
                count += 1
                Thread.sleep(2000)
                println("alice's payment status: ${pay1.status}")
                println("bob's payment status: ${pay2.status}")
                assertNotEquals(5, count, "Payment wasn't confirmed or found in 10 blocks")
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
            generator.generate(1)
            count += 1
            Thread.sleep(1000)
            assertNotEquals(10, count, "Invoice wasn't paid or found in 10 blocks")
        }
    }

    @Test
    fun createInvoice1() {
        val invId = aliceAPI.createInvoice(Currency.BTC, 100.2)
        val inv = aliceAPI.getInvoice(invId)
        assertNotNull(inv)

        val half = inv!!.amount / BigDecimal(2)
        bobAPI.sendPayment(inv.currency, half, inv.address)
        Thread.sleep(2000)
        generator.generate(1)
        Thread.sleep(2000)
        println("Received funds: ${inv.received} / ${inv.amount} in tx ${inv.txids}")

        bobAPI.sendPayment(inv.currency, half, inv.address)
        Thread.sleep(2000)
        var count = 0
        while (inv.status != InvoiceStatus.PAID) {
            generator.generate(1)
            count += 1
            Thread.sleep(1000)
            assertNotEquals(10, count, "Invoice wasn't paid or found in 10 blocks")
        }
        println("$inv :")
        println("   txs ${inv.txids}")
    }

}