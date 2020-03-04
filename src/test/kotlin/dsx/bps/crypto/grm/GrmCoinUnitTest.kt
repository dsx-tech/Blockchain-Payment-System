package dsx.bps.crypto.grm

import dsx.bps.config.currencies.GrmConfig
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.grm.datamodel.GrmInternalTxId
import dsx.bps.crypto.grm.datamodel.GrmMsgData
import dsx.bps.crypto.grm.datamodel.GrmRawMessage
import dsx.bps.crypto.grm.datamodel.GrmRawTransaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal

internal class GrmCoinUnitTest {

    private val grmConnection = Mockito.mock(GrmConnection::class.java)
    private val grmExplorer = Mockito.mock(GrmExplorer::class.java)
    private val grmCoin = GrmCoin(grmConnection, grmExplorer, javaClass.getResource("/TestBpsConfig.yaml").path)


    @Test
    @DisplayName("getBalance test")
    fun getBalanceTest() {
        grmCoin.getBalance()
        Mockito.verify(grmConnection, Mockito.only()).getBalance(grmCoin.config[GrmConfig.Coin.accountAddress])
    }

    @Test
    @DisplayName("getAddress test")
    fun getAddressTest() {
        Assertions.assertEquals(grmCoin.getAddress(), grmCoin.config[GrmConfig.Coin.accountAddress])
    }

    @Test
    @DisplayName("getTag test")
    fun getTagTest() {
        Assertions.assertTrue(grmCoin.getTag() is Int)
    }

    @Test
    @DisplayName("getTx test")
    fun getTxTest() {
        //TODO: Not implement
    }

    @Test
    @DisplayName("sendPayment test")
    fun sendPaymentTest() {
        //TODO: Not implement
    }

    @Test
    @DisplayName("getFullAccountState test")
    fun getFullAccountStateTest() {
        grmCoin.getFullAccountState()
        Mockito.verify(grmConnection, Mockito.only())
            .getFullAccountState(grmCoin.config[GrmConfig.Coin.accountAddress])
    }

    @Test
    @DisplayName("getLastInternalTxId test")
    fun getLastInternalTxIdTest() {
        grmCoin.getLastInternalTxId()
        Mockito.verify(grmConnection, Mockito.only())
            .getLastInternalTxId(grmCoin.config[GrmConfig.Coin.accountAddress])
    }

    @Test
    @DisplayName("getAccountTxs test")
    fun getAccountTxsTest() {
        val grmInternalTxId = GrmInternalTxId(10000, "hash".toByteArray())
        grmCoin.getAccountTxs(grmInternalTxId)
        Mockito.verify(grmConnection, Mockito.only())
            .getAccountTxs(grmCoin.config[GrmConfig.Coin.accountAddress], grmInternalTxId)
    }

    @Test
    @DisplayName("constructTx(grmTx: GrmRawTransaction) test")
    fun constructTxTest() {
        val msgData = Mockito.mock(GrmMsgData::class.java)
        Mockito.`when`(msgData.body).thenReturn("555".toByteArray())

        val inMsg = Mockito.mock(GrmRawMessage::class.java)
        Mockito.`when`(inMsg.value).thenReturn(5000)
        Mockito.`when`(inMsg.destination).thenReturn("destination")
        Mockito.`when`(inMsg.msgData).thenReturn(msgData)

        val transactionId = Mockito.mock(GrmInternalTxId::class.java)
        Mockito.`when`(transactionId.hash).thenReturn("hash".toByteArray())
        Mockito.`when`(transactionId.lt).thenReturn(1000)
        val grmRawTransaction = Mockito.mock(GrmRawTransaction::class.java)
        Mockito.`when`(grmRawTransaction.transactionId).thenReturn(transactionId)
        Mockito.`when`(grmRawTransaction.inMsg).thenReturn(inMsg)
        Mockito.`when`(grmRawTransaction.fee).thenReturn(1)

        val resultTx = grmCoin.constructTx(grmRawTransaction)
        Assertions.assertEquals(resultTx.currency(), grmCoin.currency)
        Assertions.assertEquals(resultTx.hash(), transactionId.hash.toString())
        Assertions.assertEquals(resultTx.index(), transactionId.lt.toInt())
        Assertions.assertEquals(resultTx.amount(), BigDecimal(5000))
        Assertions.assertEquals(resultTx.destination(), "destination")
        Assertions.assertEquals(resultTx.tag(), 555)
        Assertions.assertEquals(resultTx.fee(), BigDecimal.ONE)
        Assertions.assertEquals(resultTx.status(), TxStatus.CONFIRMED)

    }
}