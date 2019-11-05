package dsx.bps.core

import dsx.bps.core.datamodel.*
import dsx.bps.crypto.btc.BtcClient
import dsx.bps.crypto.common.CoinClient
import dsx.bps.crypto.trx.TrxClient
import dsx.bps.crypto.xrp.XrpClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal

internal class BlockchainPaymentSystemManagerUnitTest {

    private val btcClient = Mockito.mock(BtcClient::class.java)
    private val trxClient = Mockito.mock(TrxClient::class.java)
    private val xrpClient = Mockito.mock(XrpClient::class.java)
    private val coinClients: Map<Currency, CoinClient> = mapOf(Pair(Currency.BTC, btcClient),
        Pair(Currency.TRX, trxClient), Pair(Currency.XRP, xrpClient))

    private val invoiceProcessor = Mockito.mock(InvoiceProcessor::class.java)
    private val paymentProcessor = Mockito.mock(PaymentProcessor::class.java)
    private val bpsManager = BlockchainPaymentSystemManager(coinClients, invoiceProcessor, paymentProcessor)

    @Test
    @DisplayName("getBalance test")
    fun getBalanceTest(){
        Mockito.`when`(btcClient.getBalance()).thenReturn(BigDecimal.TEN)
        Assertions.assertEquals(bpsManager.getBalance(Currency.BTC), BigDecimal.TEN)
    }

    @Test
    @DisplayName("sendPayment test")
    fun sendPaymentTest(){
        val payment = Mockito.mock(Payment::class.java)
        Mockito.`when`(payment.id).thenReturn("id")
        Mockito.`when`(paymentProcessor.createPayment(Currency.BTC, BigDecimal.TEN,"testaddress",1))
            .thenReturn(payment)
        Mockito.`when`(btcClient.sendPayment(BigDecimal.TEN,"testaddress",1))
            .thenReturn(Mockito.mock(Tx::class.java))
        val resultPaymentId = bpsManager.sendPayment(Currency.BTC, BigDecimal.TEN, "testaddress", 1)
        Assertions.assertEquals(resultPaymentId, "id")
    }

    @Test
    @DisplayName("createInvoice test")
    fun createInvoiceTest(){
        val invoice = Mockito.mock(Invoice::class.java)
        Mockito.`when`(invoice.id).thenReturn("id")
        Mockito.`when`(invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", 1))
            .thenReturn(invoice)
        Mockito.`when`(btcClient.getTag()).thenReturn(1)
        Mockito.`when`(btcClient.getAddress()).thenReturn("testaddress")
        val returnInvoiceId = bpsManager.createInvoice(Currency.BTC, BigDecimal.TEN)
        Assertions.assertEquals(returnInvoiceId, "id")
    }

    @Test
    @DisplayName("getPayment test")
    fun getPaymentTest(){
        bpsManager.getPayment("id")
        Mockito.verify(paymentProcessor, Mockito.only()).getPayment("id")
    }

    @Test
    @DisplayName("getInvoice test")
    fun getInvoiceTest(){
        bpsManager.getInvoice("id")
        Mockito.verify(invoiceProcessor, Mockito.only()).getInvoice("id")
    }

    @Test
    @DisplayName("getTx test")
    fun getTxTest(){
        val txid = Mockito.mock(TxId::class.java)
        val tx = Mockito.mock(Tx::class.java)
        Mockito.`when`(btcClient.getTx(txid)).thenReturn(tx)
        val resultTx = bpsManager.getTx(Currency.BTC, txid)
        Assertions.assertEquals(resultTx, tx)
    }

    @Test
    @DisplayName("getTxs test")
    fun getTxsTest(){
        val txids = listOf(Mockito.mock(TxId::class.java))
        val txs = listOf(Mockito.mock(Tx::class.java))
        Mockito.`when`(btcClient.getTxs(txids)).thenReturn(txs)
        val resultTx = bpsManager.getTxs(Currency.BTC, txids)
        Assertions.assertEquals(resultTx, txs)
    }
}