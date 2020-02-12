package dsx.bps.core

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.InvoiceService
import dsx.bps.DBservices.TxService
import dsx.bps.config.DatabaseConfig
import dsx.bps.config.InvoiceProcessorConfig
import dsx.bps.core.datamodel.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito
import io.reactivex.disposables.Disposable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Nested
import java.io.File
import java.math.BigDecimal

internal class InvoiceProcessorUnitTest {

    private val manager: BlockchainPaymentSystemManager = Mockito.mock(BlockchainPaymentSystemManager::class.java)
    private val invoiceProcessor: InvoiceProcessor
    private val invService: InvoiceService
    private val txService: TxService
    private val testConfig: Config

    init {
        val initConfig = Config()
        val configFile = File(javaClass.getResource("/TestBpsConfig.yaml").path)
        testConfig = with (initConfig) {
            addSpec(InvoiceProcessorConfig)
            addSpec(DatabaseConfig)
            from.yaml.file(configFile)
        }

        testConfig.validateRequired()

        invoiceProcessor = InvoiceProcessor(manager, testConfig)
        invService = InvoiceService(testConfig[DatabaseConfig.connectionURL], testConfig[DatabaseConfig.driver])
        txService = TxService(testConfig[DatabaseConfig.connectionURL], testConfig[DatabaseConfig.driver])
    }

    @Test
    @DisplayName("create invoice and get invoice test")
    fun createInvoiceTest() {
        val currency = Mockito.mock(Currency::class.java)
        val invoice = invoiceProcessor.createInvoice(currency, BigDecimal.TEN,"testaddress", 1)
        Assertions.assertNotNull(invService.getBySystemId(invoice.id))
        Assertions.assertEquals(invoice.address, invService.getBySystemId(invoice.id).address)
        Assertions.assertNotNull(invoiceProcessor.getInvoice(invoice.id))
        Assertions.assertEquals(invoice, invoiceProcessor.getInvoice(invoice.id))
    }

    @Nested
    inner class OnNextTest {
        @Test
        @DisplayName("onNextTest: right tx")
        fun onNextTest1() {
            val tx = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx.currency()).thenReturn(Currency.BTC)
            Mockito.`when`(tx.destination()).thenReturn("testaddress")
            Mockito.`when`(tx.tag()).thenReturn(1)
            Mockito.`when`(tx.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx.txid()).thenReturn(TxId("hash1",1))

            txService.add(tx.status().toString(), "testaddress", 1, BigDecimal.ZERO,
                "hash1", 1, tx.currency().toString())
            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", 1)
            invoiceProcessor.onNext(tx)
            Assertions.assertEquals("paid", invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
            Assertions.assertTrue(transaction { txService.getByTxId("hash1", 1).payable ==
                    invService.getBySystemId(invoice.id).payable})
            Assertions.assertEquals(invoice.status, InvoiceStatus.PAID)
            Assertions.assertEquals(invoice.received, BigDecimal.TEN)
            Assertions.assertTrue(invoice.txids.contains(TxId("hash1", 1)))
        }

        @Test
        @DisplayName("onNextTest: wrong tx tag")
        fun onNextTest2() {
            val tx1 = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx1.currency()).thenReturn(Currency.BTC)
            Mockito.`when`(tx1.destination()).thenReturn("testaddress")
            Mockito.`when`(tx1.tag()).thenReturn(null)
            Mockito.`when`(tx1.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx1.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx1.txid()).thenReturn(TxId("hash2",2))

            txService.add(tx1.status().toString(), "testaddress", 1, BigDecimal.ZERO,
                "hash2", 2, tx1.currency().toString())
            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", 1)
            invoiceProcessor.onNext(tx1)
            Assertions.assertEquals("unpaid", invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
            Assertions.assertFalse(transaction { txService.getByTxId("hash2", 2).payable ==
                    invService.getBySystemId(invoice.id).payable})
            Assertions.assertEquals(invoice.status, InvoiceStatus.UNPAID)
            Assertions.assertEquals(invoice.received, BigDecimal.ZERO)
            Assertions.assertFalse(invoice.txids.contains(TxId("hash2", 2)))
        }

        @Test
        @DisplayName("onNextTest: wrong tx address")
        fun onNextTest3() {
            val tx1 = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx1.currency()).thenReturn(Currency.BTC)
            Mockito.`when`(tx1.destination()).thenReturn("testaddressother")
            Mockito.`when`(tx1.tag()).thenReturn(1)
            Mockito.`when`(tx1.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx1.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx1.txid()).thenReturn(TxId("hash3",3))

            txService.add(tx1.status().toString(), "testaddress", 1, BigDecimal.ZERO,
                "hash3", 3, tx1.currency().toString())
            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", 1)
            invoiceProcessor.onNext(tx1)
            Assertions.assertEquals("unpaid", invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
            Assertions.assertFalse(transaction { txService.getByTxId("hash3", 3).payable ==
                    invService.getBySystemId(invoice.id).payable})
            Assertions.assertEquals(invoice.status, InvoiceStatus.UNPAID)
            Assertions.assertEquals(invoice.received, BigDecimal.ZERO)
            Assertions.assertFalse(invoice.txids.contains(TxId("hash3", 3)))
        }

        @ParameterizedTest
        @DisplayName("onNextTest: wrong tx currency")
        @EnumSource(value = Currency::class, names = ["TRX", "XRP"])
        fun onNextTest4(currency: Currency) {
            val tx1 = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx1.currency()).thenReturn(currency)
            Mockito.`when`(tx1.destination()).thenReturn("testaddress")
            Mockito.`when`(tx1.tag()).thenReturn(1)
            Mockito.`when`(tx1.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx1.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx1.txid()).thenReturn(TxId("hash4",4))

            txService.add(tx1.status().toString(), "testaddress", 1, BigDecimal.ZERO,
                "hash4", 4, tx1.currency().toString())
            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", 1)
            invoiceProcessor.onNext(tx1)
            Assertions.assertEquals("unpaid", invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
            Assertions.assertFalse(transaction { txService.getByTxId("hash4", 4).payable ==
                    invService.getBySystemId(invoice.id).payable})
            Assertions.assertEquals(invoice.status, InvoiceStatus.UNPAID)
            Assertions.assertEquals(invoice.received, BigDecimal.ZERO)
            Assertions.assertFalse(invoice.txids.contains(TxId("hash4", 4)))
        }

        @ParameterizedTest
        @DisplayName("onNextTest: wrong tx status")
        @EnumSource(value = TxStatus::class, names = ["VALIDATING", "REJECTED"])
        fun onNextTest5(txStatus: TxStatus) {
            val tx1 = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx1.currency()).thenReturn(Currency.TRX)
            Mockito.`when`(tx1.destination()).thenReturn("testaddress")
            Mockito.`when`(tx1.tag()).thenReturn(1)
            Mockito.`when`(tx1.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx1.status()).thenReturn(txStatus)
            Mockito.`when`(tx1.txid()).thenReturn(TxId("hash5",5))

            txService.add(tx1.status().toString(), "testaddress", 1, BigDecimal.ZERO,
                "hash5", 5, tx1.currency().toString())
            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", 1)
            invoiceProcessor.onNext(tx1)
            Assertions.assertEquals("unpaid", invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
            Assertions.assertFalse(transaction { txService.getByTxId("hash5", 5).payable ==
                    invService.getBySystemId(invoice.id).payable})
            Assertions.assertEquals(invoice.status, InvoiceStatus.UNPAID)
            Assertions.assertEquals(invoice.received, BigDecimal.ZERO)
            Assertions.assertFalse(invoice.txids.contains(TxId("hash5", 5)))
        }
    }

    @Test
    fun onErrorTest() {
        val e = Mockito.mock(Throwable::class.java)
        invoiceProcessor.onError(e)
    }

    @Test
    fun onCompleteTest() {
        invoiceProcessor.onComplete()
    }

    @Test
    fun onSubscribeTest() {
        val d = Mockito.mock(Disposable::class.java)
        invoiceProcessor.onSubscribe(d)
    }
}