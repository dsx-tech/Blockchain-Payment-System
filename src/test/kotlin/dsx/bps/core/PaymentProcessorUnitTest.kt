package dsx.bps.core

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.PaymentProcessorConfig
import dsx.bps.core.datamodel.*
import dsx.bps.exception.core.payment.PaymentException
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
    private val testConfig: Config

    init {
        val initConfig = Config()
        val configFile = File(javaClass.getResource("/TestBpsConfig.yaml").path)
        testConfig = with(initConfig) {
            addSpec(PaymentProcessorConfig)
            from.yaml.file(configFile)
        }

        testConfig.validateRequired()

        paymentProcessor = PaymentProcessor(manager, testConfig)
    }

    @ParameterizedTest
    @EnumSource(value = Currency::class)
    @DisplayName("create and get payment test")
    fun createPaymentTest(currency: Currency) {
        val payment = paymentProcessor.createPayment(currency, BigDecimal.TEN, "testaddress", "1")
        val receivePayment = paymentProcessor.getPayment(payment.id)
        Assertions.assertEquals(payment, receivePayment)
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
            Mockito.`when`(tx.paymentReference()).thenReturn("1")
            Mockito.`when`(tx.fee()).thenReturn(BigDecimal.ONE)
            Mockito.`when`(tx.txid()).thenReturn(txId)
            Mockito.`when`(tx.status()).thenReturn(TxStatus.VALIDATING)

            Mockito.`when`(manager.getTx(Currency.BTC, txId)).thenReturn(tx)

            val payment = paymentProcessor.createPayment(Currency.BTC, BigDecimal.TEN, "testaddress", "1")
            Assertions.assertEquals(payment.status, PaymentStatus.PENDING)
            paymentProcessor.updatePayment(payment.id, tx)
            Assertions.assertEquals(payment.status, PaymentStatus.PROCESSING)
        }
    }
}