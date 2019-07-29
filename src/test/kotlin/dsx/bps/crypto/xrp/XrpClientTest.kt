package dsx.bps.crypto.xrp

import dsx.bps.api.BlockchainPaymentSystemAPI
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.InvoiceStatus
import dsx.bps.core.datamodel.PaymentStatus
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class XrpClientTest {

    private val cur = Currency.XRP
    private val configDir = System.getProperty("user.home") + File.separator + "bps" + File.separator
    private val aliceConfigPath = configDir + "alice.properties"
    private val bobConfigPath = configDir + "bob.properties"

    private val alice = BlockchainPaymentSystemAPI(aliceConfigPath)
    private val bob = BlockchainPaymentSystemAPI(bobConfigPath)
    private val rpc = XrpRpc("http://34.253.178.165:9009/")

    private val aliceAddress = "rHb9CJAWyB4rj91VRWn96DkukG4bwdtyTh"
    private val bobAddress = "r3i3bJYawZHojgzpzQeSbpkQp5ecXZyLrs"

    @Test
    fun getBalance() {
        println(alice.getBalance(cur))
    }

    @Test
    fun sendPayment() {
        val id = alice.sendPayment(cur, BigDecimal(8800555), bobAddress)
        Thread.sleep(2000)
        rpc.ledgerAccept()
        Thread.sleep(1000)
        val payment = alice.getPayment(id)
        assertNotNull(payment)
        var count = 0
        while (payment!!.status != PaymentStatus.SUCCEED) {
            rpc.ledgerAccept()
            count += 1
            Thread.sleep(2000)
            assertNotEquals(5, count, "Payment wasn't confirmed or found in 5 blocks")
        }
    }

    @Test
    fun createInvoiceXrp() {
        val invId = bob.createInvoice(cur, 373)
        val inv = bob.getInvoice(invId)
        assertNotNull(inv)
        println("bob's invoice:\n" +
                "   $inv\n")

        val payId = alice.sendPayment(inv!!.currency, inv.amount, inv.address, inv.tag)
        Thread.sleep(2000)
        val pay = alice.getPayment(payId)
        assertNotNull(pay)
        println("alice's payment:" +
                "   $pay\n" +
                "   [ ${pay!!.txid} ]")

        var count = 0
        while (inv.status != InvoiceStatus.PAID) {
            rpc.ledgerAccept()
            count += 1
            Thread.sleep(1000)
            println("inv: ${inv.received} / ${inv.amount}\n" +
                    "   ${inv.txids}")
            assertNotEquals(10, count, "Invoice wasn't paid or found in 10 ledgers")
        }
    }
}