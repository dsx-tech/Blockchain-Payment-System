package dsx.bps.crypto.grm

import dsx.bps.config.currencies.GrmConfig
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.grm.datamodel.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal

internal class GrmCoinUnitTest {

    private val grmConnection = Mockito.mock(GrmConnector::class.java)
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
        Assertions.assertTrue(grmCoin.getTag() is String)
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
        val grmInternalTxId = GrmInternalTxId(0L, "0000000000000000000000000000000000000000000000000000000000000000")

        Mockito.`when`(
            grmConnection.getOlderAccountTxs(
                grmCoin.config[GrmConfig.Coin.accountAddress], grmInternalTxId
            )
        ).thenReturn(GrmRawTransactions(arrayOf<GrmRawTransaction>(), grmInternalTxId))

        grmCoin.getAccountTxs(grmInternalTxId, grmInternalTxId)

        Mockito.verify(grmConnection, Mockito.only())
            .getOlderAccountTxs(grmCoin.config[GrmConfig.Coin.accountAddress], grmInternalTxId)
    }

    @Test
    @DisplayName("constructTx(grmTx: GrmRawTransaction) test")
    fun constructTxTest() {
        val msgData = Mockito.mock(GrmMsgData::class.java)
        Mockito.`when`(msgData.body).thenReturn("555")

        val inMsg = Mockito.mock(GrmRawMessage::class.java)
        Mockito.`when`(inMsg.value).thenReturn(5000)
        Mockito.`when`(inMsg.destination).thenReturn("destination")
        Mockito.`when`(inMsg.msgData).thenReturn(msgData)

        val transactionId = Mockito.mock(GrmInternalTxId::class.java)
        Mockito.`when`(transactionId.hash).thenReturn("hash")
        Mockito.`when`(transactionId.lt).thenReturn(1000)
        val grmRawTransaction = Mockito.mock(GrmRawTransaction::class.java)
        Mockito.`when`(grmRawTransaction.transactionId).thenReturn(transactionId)
        Mockito.`when`(grmRawTransaction.inMsg).thenReturn(inMsg)
        Mockito.`when`(grmRawTransaction.fee).thenReturn(1)

        val resultTx = grmCoin.constructTx(grmRawTransaction)
        Assertions.assertEquals(resultTx.currency(), grmCoin.currency)
        Assertions.assertEquals(resultTx.hash(), transactionId.hash)
        Assertions.assertEquals(resultTx.txid(), TxId(transactionId.hash, transactionId.lt))
        Assertions.assertEquals(resultTx.amount(), BigDecimal(5000))
        Assertions.assertEquals(resultTx.destination(), inMsg.destination)
        Assertions.assertEquals(resultTx.paymentReference(), msgData.body)
        Assertions.assertEquals(resultTx.fee(), BigDecimal.ONE)
        Assertions.assertEquals(resultTx.status(), TxStatus.CONFIRMED)
    }
}