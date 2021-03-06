package dsx.bps.core

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.InvoiceService
import dsx.bps.DBservices.core.TxService
import dsx.bps.TestUtils
import dsx.bps.config.DatabaseConfig
import dsx.bps.config.InvoiceProcessorConfig
import dsx.bps.core.datamodel.*
import io.reactivex.disposables.Disposable
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito
import java.io.File
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class InvoiceProcessorUnitTest {

    private val manager: BlockchainPaymentSystemManager = Mockito.mock(BlockchainPaymentSystemManager::class.java)
    private lateinit var invoiceProcessor: InvoiceProcessor
    private val datasource = Datasource()
    private lateinit var invService: InvoiceService
    private lateinit var txService: TxService
    private lateinit var testConfig: Config

    @BeforeAll
    fun setUp() {
        val initConfig = Config()
        val configPath = TestUtils.getResourcePath("TestBpsConfig.yaml")
        val configFile = File(configPath)
        testConfig = with(initConfig) {
            addSpec(InvoiceProcessorConfig)
            from.yaml.file(configFile)
        }
        testConfig.validateRequired()

        val databaseConfig = with (Config()) {
            addSpec(DatabaseConfig)
            from.yaml.file(configFile)
        }
        databaseConfig.validateRequired()

        datasource.initConnection(databaseConfig)
        txService = TxService()
        DatabaseCreation().createInvoices()
        invoiceProcessor = InvoiceProcessor(manager, testConfig, txService)
        invService = InvoiceService()
    }

    @ParameterizedTest
    @EnumSource(value = Currency::class)
    @DisplayName("create invoice and get invoice test")
    fun createInvoiceTest(currency: Currency) {
        val invoice = invoiceProcessor.createInvoice(currency, BigDecimal.TEN,"testaddress", "1")
        Assertions.assertNotNull(invService.getBySystemId(invoice.id))
        Assertions.assertEquals(invoice, invService.makeInvFromDB(invService.getBySystemId(invoice.id)))
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
            Mockito.`when`(tx.destination()).thenReturn("invtestaddress")
            Mockito.`when`(tx.paymentReference()).thenReturn("1")
            Mockito.`when`(tx.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx.txid()).thenReturn(TxId("txhash1",1))

            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "invtestaddress", "1")
            txService.add(
                tx.status(), "invtestaddress", "1", BigDecimal.TEN, BigDecimal.ZERO,
                "txhash1", 1, tx.currency()
            )
            invoiceProcessor.onNext(tx)
            Assertions.assertEquals(InvoiceStatus.PAID, invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
            Assertions.assertEquals(invoice.status, InvoiceStatus.PAID)
            Assertions.assertEquals(invoice.received, BigDecimal.TEN)
            Assertions.assertTrue(invoice.txids.contains(TxId("txhash1", 1)))
            Assertions.assertTrue(transaction {
                txService.getByTxId("txhash1", 1).cryptoAddress ==
                        invService.getBySystemId(invoice.id).cryptoAddress
            })
        }

        @Test
        @DisplayName("onNextTest: wrong tx tag")
        fun onNextTest2() {
            val tx1 = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx1.currency()).thenReturn(Currency.BTC)
            Mockito.`when`(tx1.destination()).thenReturn("testaddress")
            Mockito.`when`(tx1.paymentReference()).thenReturn(null)
            Mockito.`when`(tx1.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx1.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx1.txid()).thenReturn(TxId("hash2", 2))

            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", "1")
            invoiceProcessor.onNext(tx1)
            Assertions.assertEquals(InvoiceStatus.UNPAID, invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
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
            Mockito.`when`(tx1.paymentReference()).thenReturn("1")
            Mockito.`when`(tx1.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx1.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx1.txid()).thenReturn(TxId("hash3", 3))

            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", "1")
            invoiceProcessor.onNext(tx1)
            Assertions.assertEquals(InvoiceStatus.UNPAID, invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
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
            Mockito.`when`(tx1.paymentReference()).thenReturn("1")
            Mockito.`when`(tx1.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx1.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx1.txid()).thenReturn(TxId("hash4", 4))

            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", "1")
            invoiceProcessor.onNext(tx1)
            Assertions.assertEquals(InvoiceStatus.UNPAID, invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
            Assertions.assertEquals(invoice.status, InvoiceStatus.UNPAID)
            Assertions.assertEquals(invoice.received, BigDecimal.ZERO)
            Assertions.assertFalse(invoice.txids.contains(TxId("hash4", 4)))
        }

        @ParameterizedTest
        @DisplayName("onNextTest: wrong tx status")
        @EnumSource(value = TxStatus::class, names = ["VALIDATING", "REJECTED"])
        fun onNextTest5(txStatus: TxStatus) {
            val tx1 = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx1.currency()).thenReturn(Currency.BTC)
            Mockito.`when`(tx1.destination()).thenReturn("testaddress")
            Mockito.`when`(tx1.paymentReference()).thenReturn("1")
            Mockito.`when`(tx1.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx1.status()).thenReturn(txStatus)
            Mockito.`when`(tx1.txid()).thenReturn(TxId("hash5", 5))

            val invoice = invoiceProcessor.createInvoice(Currency.BTC, BigDecimal.TEN, "testaddress", "1")
            invoiceProcessor.onNext(tx1)
            Assertions.assertEquals(InvoiceStatus.UNPAID, invService.getBySystemId(invoice.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
            Assertions.assertEquals(invoice.status, InvoiceStatus.UNPAID)
            Assertions.assertEquals(invoice.received, BigDecimal.ZERO)
            Assertions.assertTrue(invoice.txids.contains(TxId("hash5", 5)))
        }

        @Test
        @DisplayName("onNextTest: invoices initialisation from db")
        fun onNextTest6() {
            val tx = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx.currency()).thenReturn(Currency.BTC)
            Mockito.`when`(tx.destination()).thenReturn("addr1")
            Mockito.`when`(tx.paymentReference()).thenReturn(null)
            Mockito.`when`(tx.amount()).thenReturn(BigDecimal.ONE)
            Mockito.`when`(tx.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx.txid()).thenReturn(TxId("txhashdb", 1))

            txService.add(tx.status(), tx.destination(), null, BigDecimal.ONE, BigDecimal.ZERO,
                    "txhashdb", 1, Currency.BTC)
            val invoice = invoiceProcessor.getInvoice("inv1")
            invoiceProcessor.onNext(tx)
            Assertions.assertEquals(InvoiceStatus.PAID, invService.getBySystemId(invoice!!.id).status)
            Assertions.assertEquals(invoice.received, invService.getBySystemId(invoice.id).received.setScale(0))
            Assertions.assertTrue(transaction {
                txService.getByTxId("txhashdb", 1).cryptoAddress ==
                        invService.getBySystemId(invoice.id).cryptoAddress
            })
            Assertions.assertEquals(invoice.status, InvoiceStatus.PAID)
            Assertions.assertEquals(invoice.received, BigDecimal.ONE)
            Assertions.assertTrue(invoice.txids.contains(TxId("txhashdb", 1)))
            Assertions.assertNotNull(invoiceProcessor.getInvoice("inv2"))
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