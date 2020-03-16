package dsx.bps.core

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.PaymentService
import dsx.bps.DBservices.TxService
import dsx.bps.TestUtils
import dsx.bps.config.DatabaseConfig
import dsx.bps.config.PaymentProcessorConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.exception.core.payment.PaymentException
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito
import java.io.File
import java.math.BigDecimal

internal class PaymentProcessorUnitTest {

    private val manager = Mockito.mock(BlockchainPaymentSystemManager::class.java)
    private val paymentProcessor: PaymentProcessor
    private val datasource = Datasource()
    private val payService: PaymentService
    private val txService: TxService
    private val testConfig: Config

    init {
        val initConfig = Config()
        val configPath = TestUtils.getResourcePath("TestBpsConfig.yaml")
        val configFile = File(configPath)
        testConfig = with(initConfig) {
            addSpec(PaymentProcessorConfig)
            from.yaml.file(configFile)
        }
        testConfig.validateRequired()

        val databaseConfig = with (Config()) {
            addSpec(DatabaseConfig)
            from.yaml.file(configFile)
        }
        databaseConfig.validateRequired()

        datasource.initConnection(databaseConfig)
        DatabaseCreation(datasource).createPayments()
        paymentProcessor = PaymentProcessor(manager, testConfig, datasource)
        payService = PaymentService(datasource)
        txService = TxService(datasource)
    }

    @ParameterizedTest
    @EnumSource(value = Currency::class)
    @DisplayName("create and get payment test")
    fun createPaymentTest(currency: Currency) {
        val payment = paymentProcessor.createPayment(currency, BigDecimal.TEN, "testaddress", 1)
        val receivePayment = paymentProcessor.getPayment(payment.id)
        Assertions.assertEquals(payment, payService.makePaymentFromDB(payService.getBySystemId(payment.id)))
        Assertions.assertEquals(payment, receivePayment)
    }

    @Test
    @DisplayName("update payment with db initialisation")
    fun updatePaymentFromDB() {
        val txId = Mockito.mock(TxId::class.java)
        Mockito.`when`(txId.hash).thenReturn("txhash1")
        Mockito.`when`(txId.index).thenReturn(1)

        val tx = Mockito.mock(Tx::class.java)
        Mockito.`when`(tx.txid()).thenReturn(TxId("txhash1", 1))
        Mockito.`when`(tx.fee()).thenReturn(BigDecimal.ZERO)
        Mockito.`when`(tx.currency()).thenReturn(Currency.BTC)
        Mockito.`when`(tx.amount()).thenReturn(BigDecimal.ONE)
        Mockito.`when`(tx.destination()).thenReturn("addr1")
        Mockito.`when`(tx.tag()).thenReturn(null)
        Mockito.`when`(tx.fee()).thenReturn(BigDecimal.ZERO)
        Mockito.`when`(tx.txid()).thenReturn(txId)
        Mockito.`when`(tx.status()).thenReturn(TxStatus.VALIDATING)

        Mockito.`when`(manager.getTx(Currency.BTC, txId)).thenReturn(tx)

        txService.add(tx.status(), tx.destination(), tx.tag(), tx.amount(), tx.fee(),
            "txhash1", 1, tx.currency())
        val payment = paymentProcessor.getPayment("pay1")
        Assertions.assertEquals(PaymentStatus.PENDING, payService.getBySystemId(payment!!.id).status)
        Assertions.assertEquals(payment.status, PaymentStatus.PENDING)
        paymentProcessor.updatePayment(payment.id, tx)
        Assertions.assertEquals(PaymentStatus.PROCESSING, payService.getBySystemId(payment.id).status)
        Assertions.assertEquals(payment.status, PaymentStatus.PROCESSING)
        Assertions.assertTrue(transaction { txService.getByTxId("txhash1", 1).payable ==
                payService.getBySystemId(payment.id).payable})
        Assertions.assertNotNull(paymentProcessor.getPayment("pay1"))
    }

    @Nested
    inner class UpdatePayment {

        @Test
        @DisplayName("update a nonexistent payment")
        fun updatePayment1() {
            Assertions.assertThrows(PaymentException::class.java) {
                paymentProcessor.updatePayment("", Mockito.mock(Tx::class.java))
            }
        }

        @Test
        @DisplayName("update pending payment")
        fun updatePayment2() {
            val txId = Mockito.mock(TxId::class.java)
            Mockito.`when`(txId.hash).thenReturn("hash")
            Mockito.`when`(txId.index).thenReturn(1)

            val tx = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx.txid()).thenReturn(TxId("hash", 1))
            Mockito.`when`(tx.fee()).thenReturn(BigDecimal.ZERO)
            Mockito.`when`(tx.currency()).thenReturn(Currency.BTC)
            Mockito.`when`(tx.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx.destination()).thenReturn("testaddress")
            Mockito.`when`(tx.tag()).thenReturn(1)
            Mockito.`when`(tx.fee()).thenReturn(BigDecimal.ONE)
            Mockito.`when`(tx.txid()).thenReturn(txId)
            Mockito.`when`(tx.status()).thenReturn(TxStatus.VALIDATING)

            Mockito.`when`(manager.getTx(Currency.BTC, txId)).thenReturn(tx)

            txService.add(tx.status(), tx.destination(), tx.tag(), tx.amount(), tx.fee(),
                "hash", 1, tx.currency())
            val payment = paymentProcessor.createPayment(Currency.BTC, BigDecimal.TEN, "testaddress", 1)
            Assertions.assertEquals(PaymentStatus.PENDING, payService.getBySystemId(payment.id).status)
            Assertions.assertEquals(payment.status, PaymentStatus.PENDING)
            paymentProcessor.updatePayment(payment.id, tx)
            Assertions.assertEquals(PaymentStatus.PROCESSING, payService.getBySystemId(payment.id).status)
            Assertions.assertEquals(payment.status, PaymentStatus.PROCESSING)
            Assertions.assertTrue(transaction { txService.getByTxId("hash", 1).payable ==
                    payService.getBySystemId(payment.id).payable})
        }
    }
}