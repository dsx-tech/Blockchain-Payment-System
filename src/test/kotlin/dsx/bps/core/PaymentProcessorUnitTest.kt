package dsx.bps.core

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito
import java.lang.AssertionError
import java.math.BigDecimal

internal class PaymentProcessorUnitTest {

    private val manager = Mockito.mock(BlockchainPaymentSystemManager::class.java)
    private val paymentProcessor = PaymentProcessor(manager)

    @ParameterizedTest
    @EnumSource(value = Currency::class)
    @DisplayName("create and get payment test")
    fun createPaymentTest(currency: Currency){
        val payment = paymentProcessor.createPayment(currency, BigDecimal.TEN, "testaddress", 1)
        val receivePayment = paymentProcessor.getPayment(payment.id)
        Assertions.assertEquals(payment, receivePayment)
    }

    @Nested
    inner class UpdatePayment{
        @Test
        @DisplayName("update a nonexistent payment")
        fun updatePayment1(){
            Assertions.assertThrows(AssertionError::class.java){
                paymentProcessor.updatePayment("",Mockito.mock(Tx::class.java))
            }
        }

        @Test
        @DisplayName("update pending payment")
        fun updatePayment2(){
            val tx = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx.txid()).thenReturn(TxId("hash", 1))
            Mockito.`when`(tx.fee()).thenReturn(BigDecimal.ZERO)

            val payment = paymentProcessor.createPayment(Currency.BTC, BigDecimal.TEN, "testaddress", 1)
            Assertions.assertEquals(payment.status, PaymentStatus.PENDING)
            paymentProcessor.updatePayment(payment.id, tx)
            Assertions.assertEquals(payment.status, PaymentStatus.PROCESSING)
        }
    }
}