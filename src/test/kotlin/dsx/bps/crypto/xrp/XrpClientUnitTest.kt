package dsx.bps.crypto.xrp

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.PaymentService
import dsx.bps.DBservices.core.TxService
import dsx.bps.TestUtils
import dsx.bps.config.DatabaseConfig
import dsx.bps.config.currencies.XrpConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.PaymentStatus
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.xrp.datamodel.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.File
import java.math.BigDecimal

internal class XrpClientUnitTest {

    private val xrpRpc = Mockito.mock(XrpRpc::class.java)
    private val xrpBlockchainListener = Mockito.mock(XrpExplorer::class.java)
    private val datasource = Datasource()
    private val payService: PaymentService
    private val xrpClient: XrpCoin
    private val testConfig: Config

    init {
        val initConfig = Config()
        val configPath = TestUtils.getResourcePath("TestBpsConfig.yaml")
        val configFile = File(configPath)
        testConfig = with(initConfig) {
            addSpec(XrpConfig)
            from.yaml.file(configFile)
        }
        testConfig.validateRequired()

        val databaseConfig = with(Config()) {
            addSpec(DatabaseConfig)
            from.yaml.file(configFile)
        }
        databaseConfig.validateRequired()

        datasource.initConnection(databaseConfig)
        payService = PaymentService()
        xrpClient = XrpCoin(
            xrpRpc, xrpBlockchainListener,
            configPath, TxService()
        )
    }

    @Test
    @DisplayName("getBalance test")
    fun getBalanceTest() {
        xrpClient.getBalance()
        Mockito.verify(xrpRpc, Mockito.only()).getBalance(testConfig[XrpConfig.Coin.account])
    }

    @Test
    @DisplayName("getAddress test")
    fun getAddressTest() {
        Assertions.assertEquals(xrpClient.getAddress(), testConfig[XrpConfig.Coin.account])
    }

    @Test
    @DisplayName("getTag test")
    fun getTagTest() {
        Assertions.assertTrue(xrpClient.getTag() is String)
    }

    @Test
    @DisplayName("getTx test")
    fun getTxTest() {
        val txid = Mockito.mock(TxId::class.java)
        Mockito.`when`(txid.hash).thenReturn("hash")

        val xrpAmount = Mockito.mock(XrpAmount::class.java)
        Mockito.`when`(xrpAmount.value).thenReturn(BigDecimal.TEN)
        Mockito.`when`(xrpAmount.currency).thenReturn(xrpClient.currency.name)
        val xrpTxMeta = Mockito.mock(XrpTxMeta::class.java)
        Mockito.`when`(xrpTxMeta.deliveredAmount).thenReturn(xrpAmount)
        val xrpTx = Mockito.mock(XrpTx::class.java)
        Mockito.`when`(xrpTx.fee).thenReturn("1")
        Mockito.`when`(xrpTx.meta).thenReturn(xrpTxMeta)
        Mockito.`when`(xrpTx.hash).thenReturn("hash")
        Mockito.`when`(xrpTx.sequence).thenReturn(1)
        Mockito.`when`(xrpTx.validated).thenReturn(true)

        Mockito.`when`(xrpRpc.getTransaction("hash")).thenReturn(xrpTx)
        xrpClient.getTx(txid)
    }

    @Test
    @DisplayName("sendPayment test")
    fun sendPaymentTest() {
        Mockito.`when`(xrpRpc.getTxCost()).thenReturn(BigDecimal.ONE)
        Mockito.`when`(xrpRpc.getSequence(testConfig[XrpConfig.Coin.account])).thenReturn(1)

        val xrpTxPayment = XrpTxPayment(
            testConfig[XrpConfig.Coin.account],
            BigDecimal.TEN, "testaddress", "1", 1, 1
        )

        Mockito.`when`(xrpRpc.sign(testConfig[XrpConfig.Coin.privateKey], xrpTxPayment)).thenReturn("signedtx")

        val xrpAmount = Mockito.mock(XrpAmount::class.java)
        Mockito.`when`(xrpAmount.value).thenReturn(BigDecimal.TEN)
        Mockito.`when`(xrpAmount.currency).thenReturn(xrpClient.currency.name)
        val xrpTxMeta = Mockito.mock(XrpTxMeta::class.java)
        Mockito.`when`(xrpTxMeta.deliveredAmount).thenReturn(xrpAmount)
        val xrpTx = Mockito.mock(XrpTx::class.java)
        Mockito.`when`(xrpTx.fee).thenReturn("1")
        Mockito.`when`(xrpTx.meta).thenReturn(xrpTxMeta)
        Mockito.`when`(xrpTx.hash).thenReturn("hash")
        Mockito.`when`(xrpTx.sequence).thenReturn(1)
        Mockito.`when`(xrpTx.validated).thenReturn(true)
        Mockito.`when`(xrpTx.destination).thenReturn("destination")

        Mockito.`when`(xrpRpc.submit("signedtx")).thenReturn(xrpTx)

        payService.add(PaymentStatus.PENDING, "paymentId1", Currency.XRP,
            BigDecimal.TEN, "destination", "tag")

        xrpClient.sendPayment(BigDecimal.TEN, "testaddress", "1")
    }

    @Test
    @DisplayName("getLastLedger test")
    fun getLastLedgerTest() {
        xrpClient.getLastLedger()
        Mockito.verify(xrpRpc, Mockito.only()).getLastLedger()
    }

    @Test
    @DisplayName("getLedger test")
    fun getLedgerTest() {
        xrpClient.getLedger("hash")
        Mockito.verify(xrpRpc, Mockito.only()).getLedger("hash")
    }

    @Test
    @DisplayName("getAccountTxs test")
    fun getAccountTxsTest() {
        xrpClient.getAccountTxs(1, 1)
        //default value: account
        Mockito.verify(xrpRpc, Mockito.only())
            .getAccountTxs(testConfig[XrpConfig.Coin.account], 1, 1)
    }

    @Test
    @DisplayName("constructTx(xrpAccountTx: XrpAccountTx) test")
    fun constructTxTest1() {
        val xrpAmount = Mockito.mock(XrpAmount::class.java)
        Mockito.`when`(xrpAmount.value).thenReturn(BigDecimal.TEN)
        Mockito.`when`(xrpAmount.currency).thenReturn(xrpClient.currency.name)
        val xrpTx = Mockito.mock(XrpTx::class.java)
        Mockito.`when`(xrpTx.hash).thenReturn("hash")
        Mockito.`when`(xrpTx.sequence).thenReturn(1)
        Mockito.`when`(xrpTx.fee).thenReturn("1")
        val xrpTxMeta = Mockito.mock(XrpTxMeta::class.java)
        Mockito.`when`(xrpTxMeta.deliveredAmount).thenReturn(xrpAmount)
        val xrpAccountTx = Mockito.mock(XrpAccountTx::class.java)
        Mockito.`when`(xrpAccountTx.tx).thenReturn(xrpTx)
        Mockito.`when`(xrpAccountTx.meta).thenReturn(xrpTxMeta)
        Mockito.`when`(xrpAccountTx.validated).thenReturn(true)

        val resultTx = xrpClient.constructTx(xrpAccountTx)
        Assertions.assertEquals(resultTx.currency(), xrpClient.currency)
        Assertions.assertEquals(resultTx.hash(), xrpTx.hash)
        Assertions.assertEquals(resultTx.index(), xrpTx.sequence.toLong())
        Assertions.assertEquals(resultTx.amount(), xrpAmount.value)
        Assertions.assertEquals(resultTx.paymentReference(), xrpTx.destinationTag.toString())
        Assertions.assertEquals(resultTx.destination(), xrpTx.destination)
        Assertions.assertEquals(resultTx.fee(), BigDecimal(xrpTx.fee))
        Assertions.assertEquals(resultTx.status(), TxStatus.CONFIRMED)
    }

    @Test
    @DisplayName("constructTx(xrpTx: XrpTx) test")
    fun constructTxTest2() {
        val xrpAmount = Mockito.mock(XrpAmount::class.java)
        Mockito.`when`(xrpAmount.value).thenReturn(BigDecimal.TEN)
        Mockito.`when`(xrpAmount.currency).thenReturn(xrpClient.currency.name)
        val xrpTxMeta = Mockito.mock(XrpTxMeta::class.java)
        Mockito.`when`(xrpTxMeta.deliveredAmount).thenReturn(xrpAmount)
        val xrpTx = Mockito.mock(XrpTx::class.java)
        Mockito.`when`(xrpTx.fee).thenReturn("1")
        Mockito.`when`(xrpTx.meta).thenReturn(xrpTxMeta)
        Mockito.`when`(xrpTx.hash).thenReturn("hash")
        Mockito.`when`(xrpTx.sequence).thenReturn(1)
        Mockito.`when`(xrpTx.validated).thenReturn(true)

        val resultTx = xrpClient.constructTx(xrpTx)
        Assertions.assertEquals(resultTx.currency(), xrpClient.currency)
        Assertions.assertEquals(resultTx.hash(), xrpTx.hash)
        Assertions.assertEquals(resultTx.index(), xrpTx.sequence.toLong())
        Assertions.assertEquals(resultTx.amount(), xrpAmount.value)
        Assertions.assertEquals(resultTx.destination(), xrpTx.destination)
        Assertions.assertEquals(resultTx.paymentReference(), xrpTx.destinationTag.toString())
        Assertions.assertEquals(resultTx.fee(), BigDecimal(xrpTx.fee))
        Assertions.assertEquals(resultTx.status(), TxStatus.CONFIRMED)
    }
}