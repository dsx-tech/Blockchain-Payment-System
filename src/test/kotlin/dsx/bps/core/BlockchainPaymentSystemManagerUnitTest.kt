package dsx.bps.core

import dsx.bps.core.datamodel.*
import dsx.bps.crypto.CoinsManager
import io.reactivex.subjects.PublishSubject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal

internal class BlockchainPaymentSystemManagerUnitTest {

    private val coinsManager: CoinsManager = Mockito.mock(CoinsManager::class.java)
    private val invoiceProcessor = Mockito.mock(InvoiceProcessor::class.java)
    private val paymentProcessor = Mockito.mock(PaymentProcessor::class.java)
    private val depositAccountProcessor = Mockito.mock(DepositAccountProcessor::class.java)
    private val bpsManager: BlockchainPaymentSystemManager

    init {
        Mockito.`when`(coinsManager.getAllEmitters()).thenReturn(PublishSubject.create())
        bpsManager = BlockchainPaymentSystemManager(coinsManager, invoiceProcessor, paymentProcessor, depositAccountProcessor)
    }

    @Test
    @DisplayName("getBalance pick the right Coin test")
    fun getBalanceTest() {
        bpsManager.getBalance(Currency.BTC)
        Mockito.verify(coinsManager).getBalance(Currency.BTC)
    }

    @Test
    @DisplayName("sendPayment test")
    fun sendPaymentTest() {
        val payment = Mockito.mock(Payment::class.java)
        Mockito.`when`(payment.id).thenReturn("id")
        Mockito.`when`(paymentProcessor.createPayment(Currency.BTC, BigDecimal.TEN, "testaddress", "1"))
            .thenReturn(payment)
        Mockito.`when`(coinsManager.sendPayment(Currency.BTC, BigDecimal.TEN, "testaddress", "1"))
            .thenReturn(Mockito.mock(Tx::class.java))
        val resultPaymentId = bpsManager.sendPayment(Currency.BTC, BigDecimal.TEN, "testaddress", "1")
        Assertions.assertEquals(resultPaymentId, "id")
    }

    @Test
    @DisplayName("createInvoice test")
    fun createInvoiceTest() {
        val invoice = Mockito.mock(Invoice::class.java)
        Mockito.`when`(invoice.id).thenReturn("id")
        Mockito.`when`(invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", "1"))
            .thenReturn(invoice)
        Mockito.`when`(coinsManager.getTag(Currency.BTC)).thenReturn("1")
        Mockito.`when`(coinsManager.getAddress(Currency.BTC)).thenReturn("testaddress")
        val returnInvoiceId = bpsManager.createInvoice(Currency.BTC, BigDecimal.TEN)
        Assertions.assertEquals(returnInvoiceId, "id")
    }

    @Test
    @DisplayName("getPayment test")
    fun getPaymentTest() {
        bpsManager.getPayment("id")
        Mockito.verify(paymentProcessor, Mockito.only()).getPayment("id")
    }

    @Test
    @DisplayName("getInvoice test")
    fun getInvoiceTest() {
        bpsManager.getInvoice("id")
        Mockito.verify(invoiceProcessor, Mockito.only()).getInvoice("id")
    }

    @Test
    @DisplayName("getTx test")
    fun getTxTest() {
        val txid = Mockito.mock(TxId::class.java)
        val tx = Mockito.mock(Tx::class.java)
        Mockito.`when`(coinsManager.getTx(Currency.BTC, txid)).thenReturn(tx)

        val resultTx = bpsManager.getTx(Currency.BTC, txid)
        Assertions.assertEquals(resultTx, tx)
    }

    @Test
    @DisplayName("getTxs test")
    fun getTxsTest() {
        val txids = listOf(Mockito.mock(TxId::class.java))
        val txs = listOf(Mockito.mock(Tx::class.java))
        Mockito.`when`(coinsManager.getTxs(Currency.BTC, txids)).thenReturn(txs)
        val resultTx = bpsManager.getTxs(Currency.BTC, txids)
        Assertions.assertEquals(resultTx, txs)
    }
}