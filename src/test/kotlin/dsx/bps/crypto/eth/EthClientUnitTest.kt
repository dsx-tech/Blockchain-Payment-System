package dsx.bps.crypto.eth

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.EthConfig
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.eth.datamodel.EthBlock
import dsx.bps.crypto.eth.datamodel.EthTx
import dsx.bps.crypto.eth.datamodel.EthTxReceipt
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.File
import java.math.BigDecimal

internal class EthClientUnitTest {

    private val ethRpc = Mockito.mock(EthRpc::class.java)
    private val ethBlockchainListener = Mockito.mock(EthExplorer::class.java)
    private val ethClient = EthCoin(ethRpc, ethBlockchainListener,
        javaClass.getResource("/TestBpsConfig.yaml").path)
    private val testConfig: Config

    init {
        val initConfig = Config()
        val configFile = File(javaClass.getResource("/TestBpsConfig.yaml").path)
        testConfig = with (initConfig) {
            addSpec(EthConfig)
            from.yaml.file(configFile)
        }
        testConfig.validateRequired()
    }

    @Test
    @DisplayName("getBalance test")
    fun getBalanceTest() {
        ethClient.getBalance()
        Mockito.verify(ethRpc, Mockito.only()).getBalance(testConfig[EthConfig.Coin.accountAddress])
    }

    @Test
    @DisplayName("getAddress test")
    fun getAddressTest() {
        Assertions.assertEquals(ethClient.getAddress(), testConfig[EthConfig.Coin.accountAddress])
    }

    @Test
    @DisplayName("getTag test")
    fun getTagTest() {
        val randomInt = ethClient.getTag()
        Assertions.assertTrue(randomInt is Int)
    }

    @Test
    @DisplayName("getTx test")
    fun getTxTest() {
        val txidConfirmed = Mockito.mock(TxId::class.java)
        Mockito.`when`(txidConfirmed.hash).thenReturn("hash1")

        val txidValidating = Mockito.mock(TxId::class.java)
        Mockito.`when`(txidValidating.hash).thenReturn("hash2")

        val ethTxConfirmed = mockForConstructConfirmedTx()
        val ethTxValidating = mockForConstructValidatingTx()

        val latestBlock = Mockito.mock(EthBlock::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(latestBlock)
        Mockito.`when`(latestBlock.number).thenReturn("0x5")

        val txRt = Mockito.mock(EthTxReceipt::class.java)
        Mockito.`when`(ethRpc.getTransactionReceipt("hash1")).thenReturn(txRt)
        Mockito.`when`(txRt.gasUsed).thenReturn("0x470")

        Mockito.`when`(ethRpc.getTransactionByHash("hash1")).thenReturn(ethTxConfirmed)
        Mockito.`when`(ethRpc.getTransactionByHash("hash2")).thenReturn(ethTxValidating)

        val txConfirmed = ethClient.getTx(txidConfirmed)
        val txValidating = ethClient.getTx(txidValidating)
        Assertions.assertNotEquals(txConfirmed.fee(),txValidating.fee())
        Assertions.assertEquals(txConfirmed.status(),TxStatus.CONFIRMED)
        Assertions.assertEquals(txValidating.status(),TxStatus.VALIDATING)
        println("expected fee is ${txValidating.fee()} Wei")
        println("real fee is ${txConfirmed.fee()} Wei")
    }

    @Test
    @DisplayName("getLatestBlock test")
    fun getLatestBlockTest() {
        ethClient.getLatestBlock()
        Mockito.verify(ethRpc, Mockito.only()).getLatestBlock()
    }

    @Test
    @DisplayName("getBlockByHash test")
    fun getBlockByHash() {
        ethClient.getBlockByHash("hash")
        Mockito.verify(ethRpc, Mockito.only()).getBlockByHash("hash")
    }

    @Test
    @DisplayName("sendPaymentSuccess test")
    fun sendPaymentSuccessTest() {
        val ethTx = mockForConstructValidatingTx()

        Mockito.`when`(ethRpc.sendTransaction(__from = testConfig[EthConfig.Coin.accountAddress],
            __to = "testaddress", __value = BigDecimal.ONE)).thenReturn("hash2")

        val latestBlock = Mockito.mock(EthBlock::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(latestBlock)
        Mockito.`when`(latestBlock.number).thenReturn("0x5")

        Mockito.`when`(ethRpc.getTransactionByHash("hash2")).thenReturn(ethTx)

        val resTx = ethClient.sendPayment(BigDecimal.ONE,"testaddress")
        Assertions.assertEquals(resTx.status(), TxStatus.VALIDATING)
    }

    @Test
    @DisplayName("constructConfirmedTx test")
    fun constructConfirmedTxTest() {
        val ethTx = mockForConstructConfirmedTx()

        val latestBlock = Mockito.mock(EthBlock::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(latestBlock)
        Mockito.`when`(latestBlock.number).thenReturn("0x5")

        val txRt = Mockito.mock(EthTxReceipt::class.java)
        Mockito.`when`(ethRpc.getTransactionReceipt("hash1")).thenReturn(txRt)
        Mockito.`when`(txRt.gasUsed).thenReturn("0x470")

        Mockito.`when`(ethRpc.getTransactionByHash("hash1")).thenReturn(ethTx)

        val resultTx = ethClient.constructTx(ethTx)
        println(resultTx.amount())

        Assertions.assertEquals(resultTx.currency(), ethClient.currency)
        Assertions.assertEquals(resultTx.hash(),"hash1")
        Assertions.assertEquals((resultTx.amount()).toString(), "1.000000000000000")
        Assertions.assertEquals(resultTx.destination(), "to")
        Assertions.assertEquals(resultTx.fee(), 1454080.toBigDecimal())
        Assertions.assertEquals(resultTx.status(), TxStatus.CONFIRMED)
    }

    @Test
    @DisplayName("constructValidatingTx test")
    fun constructValidatingTxTest() {
        val ethTx = mockForConstructValidatingTx()

        val latestBlock = Mockito.mock(EthBlock::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(latestBlock)
        Mockito.`when`(latestBlock.number).thenReturn("0x5")


        Mockito.`when`(ethRpc.getTransactionByHash("hash2")).thenReturn(ethTx)

        val resultTx = ethClient.constructTx(ethTx)
        println(resultTx.amount())

        Assertions.assertEquals(resultTx.currency(), ethClient.currency)
        Assertions.assertEquals(resultTx.hash(),"hash2")
        Assertions.assertEquals((resultTx.amount()).toString(), "1.000000000000000")
        Assertions.assertEquals(resultTx.destination(), "to")
        Assertions.assertEquals(resultTx.fee(), 2949120.toBigDecimal())
        Assertions.assertEquals(resultTx.status(), TxStatus.VALIDATING)
    }

    private fun mockForConstructConfirmedTx(): EthTx {
        val ethTx = Mockito.mock(EthTx::class.java)
        Mockito.`when`(ethTx.blockNumber).thenReturn("0x1")
        Mockito.`when`(ethTx.hash).thenReturn("hash1")
        Mockito.`when`(ethTx.to).thenReturn("to")
        Mockito.`when`(ethTx.value).thenReturn("0xDE0B6B3A7640000") //1 ether
        Mockito.`when`(ethTx.gas).thenReturn("0x900")
        Mockito.`when`(ethTx.gasPrice).thenReturn("0x500")

        return ethTx
    }

    private fun mockForConstructValidatingTx(): EthTx {
        val ethTx = Mockito.mock(EthTx::class.java)
        Mockito.`when`(ethTx.blockNumber).thenReturn("0x4")
        Mockito.`when`(ethTx.hash).thenReturn("hash2")
        Mockito.`when`(ethTx.to).thenReturn("to")
        Mockito.`when`(ethTx.value).thenReturn("0xDE0B6B3A7640000") //1 ether
        Mockito.`when`(ethTx.gas).thenReturn("0x900")
        Mockito.`when`(ethTx.gasPrice).thenReturn("0x500")

        return ethTx
    }
}