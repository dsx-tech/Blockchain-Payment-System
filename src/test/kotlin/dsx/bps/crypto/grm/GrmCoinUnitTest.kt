package dsx.bps.crypto.grm

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.TxService
import dsx.bps.TestUtils
import dsx.bps.config.DatabaseConfig
import dsx.bps.config.currencies.GrmConfig
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.grm.datamodel.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.File
import java.math.BigDecimal

internal class GrmCoinUnitTest {

    private val grmConnector = Mockito.mock(GrmConnector::class.java)
    private val grmExplorer = Mockito.mock(GrmExplorer::class.java)
    private val datasource = Datasource()
    private val grmCoin: GrmCoin
    private val txService: TxService

    init {

        val configPath = TestUtils.getResourcePath("TestBpsConfig.yaml")
        val configFile = File(configPath)
        val databaseConfig = with(Config()) {
            addSpec(DatabaseConfig)
            from.yaml.file(configFile)
        }
        databaseConfig.validateRequired()

        datasource.initConnection(databaseConfig)
        txService = TxService(datasource)
        grmCoin = GrmCoin(
            grmConnector, grmExplorer,
            configPath,
            datasource, txService
        )
    }


    @Test
    @DisplayName("getBalance test")
    fun getBalanceTest() {
        grmCoin.getBalance()
        Mockito.verify(grmConnector, Mockito.only()).getBalance(grmCoin.config[GrmConfig.Coin.accountAddress])
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
        val txId = TxId("test", 1)
        Mockito.`when`(
            grmConnector.getTransaction(
                grmCoin.config[GrmConfig.Coin.accountAddress], txId
            )
        ).thenReturn(Mockito.mock(GrmRawTransaction::class.java))

        grmCoin.getTx(txId)
        Mockito.verify(grmConnector, Mockito.only())
            .getTransaction(grmCoin.config[GrmConfig.Coin.accountAddress], txId)
    }

    @Test
    @DisplayName("sendPayment test")
    fun sendPaymentTest() {
        val amount = BigDecimal.TEN
        val destinationAddress = "destination_address"
        val tag = "tag"
        val testHash = "test_hash"
        val newGrmInternalTxId = GrmInternalTxId(10, testHash)
        val lastGrmInternalTxId = GrmInternalTxId(8, "hash_body")
        val ourAddress = grmCoin.config[GrmConfig.Coin.accountAddress]

        Mockito.`when`(grmConnector.getLastInternalTxId(ourAddress))
            .thenReturn(lastGrmInternalTxId)
        Mockito.`when`(grmConnector.getLastInternalTxId(destinationAddress))
            .thenReturn(GrmInternalTxId(0, ""))

        val grmQueryInfo = GrmQueryInfo(1, 2000, "test_hash")
        Mockito.`when`(
            grmConnector.sendPaymentQuery(
                ourAddress,
                grmCoin.config[GrmConfig.Coin.privateKey],
                grmCoin.config[GrmConfig.Coin.localPassword],
                amount,
                destinationAddress,
                tag,
                grmCoin.config[GrmConfig.Coin.paymentQueryTimeLimit]
            )
        ).thenReturn(grmQueryInfo)

        val accountState = GrmFullAccountState(
            BigDecimal.ONE, newGrmInternalTxId,
            GrmBlockIdExt(0, 0, 0, "", ""), 30
        )
        Mockito.`when`(
            grmConnector.getFullAccountState(
                ourAddress
            )
        ).thenReturn(accountState)

        val inMsg = GrmRawMessage("", ourAddress, 0, 0, 0, 10, testHash, "")
        val outMsg = GrmRawMessage(
            ourAddress, destinationAddress,
            amount.toLong(), 0, 0, 10, "", tag
        )

        val grmTx1 = GrmRawTransaction(
            1000, "",
            newGrmInternalTxId, 0, 0, 0, inMsg, arrayOf(outMsg)
        )

        val grmTx2 = GrmRawTransaction(
            999, "",
            lastGrmInternalTxId, 0, 0, 0,
            Mockito.mock(GrmRawMessage::class.java),
            arrayOf(Mockito.mock(GrmRawMessage::class.java))
        )

        Mockito.`when`(grmConnector.getOlderAccountTxs(ourAddress, newGrmInternalTxId))
            .thenReturn(
                GrmRawTransactions(
                    arrayOf(grmTx1, grmTx2),
                    Mockito.mock(GrmInternalTxId::class.java)
                )
            )

        grmCoin.sendPayment(amount, destinationAddress, tag)
    }

    @Test
    @DisplayName("getFullAccountState test")
    fun getFullAccountStateTest() {
        grmCoin.getFullAccountState()
        Mockito.verify(grmConnector, Mockito.only())
            .getFullAccountState(grmCoin.config[GrmConfig.Coin.accountAddress])
    }

    @Test
    @DisplayName("getLastInternalTxId test")
    fun getLastInternalTxIdTest() {
        grmCoin.getLastInternalTxId()
        Mockito.verify(grmConnector, Mockito.only())
            .getLastInternalTxId(grmCoin.config[GrmConfig.Coin.accountAddress])
    }

    @Test
    @DisplayName("getAccountTxs test")
    fun getAccountTxsTest() {
        val grmInternalTxId = GrmInternalTxId(0L, "0000000000000000000000000000000000000000000000000000000000000000")

        Mockito.`when`(
            grmConnector.getOlderAccountTxs(
                grmCoin.config[GrmConfig.Coin.accountAddress], grmInternalTxId
            )
        ).thenReturn(GrmRawTransactions(arrayOf<GrmRawTransaction>(), grmInternalTxId))

        grmCoin.getAccountTxs(grmCoin.config[GrmConfig.Coin.accountAddress], grmInternalTxId, grmInternalTxId)

        Mockito.verify(grmConnector, Mockito.only())
            .getOlderAccountTxs(grmCoin.config[GrmConfig.Coin.accountAddress], grmInternalTxId)
    }

    @Test
    @DisplayName("constructTx(grmTx: GrmRawTransaction) test")
    fun constructTxTest() {
        val inMsg = Mockito.mock(GrmRawMessage::class.java)
        Mockito.`when`(inMsg.value).thenReturn(5000)
        Mockito.`when`(inMsg.destination).thenReturn("destination")
        Mockito.`when`(inMsg.msgText).thenReturn("555")

        val transactionId = Mockito.mock(GrmInternalTxId::class.java)
        Mockito.`when`(transactionId.hash).thenReturn("hash")
        Mockito.`when`(transactionId.lt).thenReturn(1000)
        val grmRawTransaction = Mockito.mock(GrmRawTransaction::class.java)
        Mockito.`when`(grmRawTransaction.transactionId).thenReturn(transactionId)
        Mockito.`when`(grmRawTransaction.inMsg).thenReturn(inMsg)
        Mockito.`when`(grmRawTransaction.fee).thenReturn(1)
        Mockito.`when`(grmRawTransaction.outMsg).thenReturn(arrayOf<GrmRawMessage>())

        val resultTx = grmCoin.constructDepositTx(grmRawTransaction)
        Assertions.assertEquals(resultTx.currency(), grmCoin.currency)
        Assertions.assertEquals(resultTx.hash(), transactionId.hash)
        Assertions.assertEquals(resultTx.txid(), TxId(transactionId.hash, transactionId.lt))
        Assertions.assertEquals(resultTx.amount(), BigDecimal(5000))
        Assertions.assertEquals(resultTx.destination(), inMsg.destination)
        Assertions.assertEquals(resultTx.paymentReference(), "555")
        Assertions.assertEquals(resultTx.fee(), BigDecimal.ONE)
        Assertions.assertEquals(resultTx.status(), TxStatus.CONFIRMED)
    }
}