package dsx.bps.crypto.eth

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.TxService
import dsx.bps.TestUtils
import dsx.bps.config.currencies.EthConfig
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.methods.response.EthBlock
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.utils.Convert
import java.io.File
import java.math.BigDecimal

internal class EthClientUnitTest {

    private val ethRpc = Mockito.mock(EthRpc::class.java)
    private val ethBlockchainListener = Mockito.mock(EthExplorer::class.java)
    private val ethClient: EthCoin
    private val testConfig: Config

    init {

        val initConfig = Config()
        val configPath = TestUtils.getResourcePath("TestBpsConfig.yaml")
        val configFile = File(configPath)
        testConfig = with(initConfig) {
            addSpec(EthConfig)
            from.yaml.file(configFile)
        }
        testConfig.validateRequired()
        Mockito.`when`(ethRpc.getTransactionCount(testConfig[EthConfig.Coin.accountAddress]))
            .thenReturn(0.toBigInteger())
        val datasource = Mockito.mock(Datasource::class.java)
        val txServ = Mockito.mock(TxService::class.java)
        ethClient = EthCoin(
            ethRpc, ethBlockchainListener,
            configPath, datasource, txServ
        )
    }

    @Test
    @DisplayName("getBalance test")
    fun getBalanceTest() {
        Mockito.`when`(ethRpc.getBalance(testConfig[EthConfig.Coin.accountAddress])).thenReturn(BigDecimal.ONE)
        Assertions.assertEquals(ethClient.getBalance(), BigDecimal.ONE)
    }

    @Test
    @DisplayName("getAddress test")
    fun getAddressTest() {
        Mockito.`when`(
            ethRpc.generateWalletFile(
                testConfig[EthConfig.Coin.defaultPasswordForNewAddresses],
                testConfig[EthConfig.Coin.walletsDir]
            )
        ).thenReturn("newAddress")

        val address = ethClient.getAddressWithWallet()
        Assertions.assertEquals("newAddress", address)
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

        val latestBlock = Mockito.mock(EthBlock.Block::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(latestBlock)
        Mockito.`when`(latestBlock.number).thenReturn(5.toBigInteger())
        Mockito.`when`(latestBlock.numberRaw).thenReturn("0x5")

        val txRt = Mockito.mock(TransactionReceipt::class.java)
        Mockito.`when`(ethRpc.getTransactionReceiptByHash("hash1")).thenReturn(txRt)
        Mockito.`when`(txRt.gasUsed).thenReturn(470.toBigInteger())

        Mockito.`when`(ethRpc.getTransactionByHash("hash1")).thenReturn(ethTxConfirmed)
        Mockito.`when`(ethRpc.getTransactionByHash("hash2")).thenReturn(ethTxValidating)

        val txConfirmed = ethClient.getTx(txidConfirmed)
        val txValidating = ethClient.getTx(txidValidating)
        Assertions.assertNotEquals(txConfirmed.fee(), txValidating.fee())
        Assertions.assertEquals(txConfirmed.status(), TxStatus.CONFIRMED)
        Assertions.assertEquals(txValidating.status(), TxStatus.VALIDATING)
    }

    @Test
    @DisplayName("getLatestBlock test")
    fun getLatestBlockTest() {
        val lastBlock = Mockito.mock(EthBlock.Block::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(lastBlock)
        Assertions.assertEquals(ethClient.getLatestBlock(), lastBlock)
    }

    @Test
    @DisplayName("getBlockByHash test")
    fun getBlockByHash() {
        val block = Mockito.mock(EthBlock.Block::class.java)
        Mockito.`when`(ethRpc.getBlockByHash("hash")).thenReturn(block)
        Assertions.assertEquals(ethClient.getBlockByHash("hash"), block)
    }

    @Test
    @DisplayName("sendPayment test")
    fun sendPaymentTest() {
        val password = testConfig[EthConfig.Coin.password]
        val pathToWallet = testConfig[EthConfig.Coin.pathToWallet]

        val ethTx = mockForConstructValidatingTx()
        val rawTx = Mockito.mock(RawTransaction::class.java)

        Mockito.`when`(ethRpc.createRawTransaction(0.toBigInteger(), toAddress = "to", value = 1.toBigDecimal()))
            .thenReturn(rawTx)
        val credentials = WalletUtils.loadCredentials(password, pathToWallet)
        Mockito.`when`(ethRpc.signTransaction(rawTx, credentials)).thenReturn("signedHex")
        Mockito.`when`(ethRpc.sendTransaction("signedHex")).thenReturn("hash2")

        val latestBlock = Mockito.mock(EthBlock.Block::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(latestBlock)
        Mockito.`when`(latestBlock.number).thenReturn(5.toBigInteger())

        Mockito.`when`(ethRpc.getTransactionByHash("hash2")).thenReturn(ethTx)

        val resTx = ethClient.sendPayment(BigDecimal.ONE, "to")
        Assertions.assertEquals(resTx.status(), TxStatus.VALIDATING)
    }

    @Test
    @DisplayName("check nonce work")
    fun sendSecondPayment() {
        sendPaymentTest()
        val password = testConfig[EthConfig.Coin.password]
        val pathToWallet = testConfig[EthConfig.Coin.pathToWallet]

        val ethTx = mockForConstructValidatingTx()
        val rawTx = Mockito.mock(RawTransaction::class.java)

        Mockito.`when`(ethRpc.createRawTransaction(1.toBigInteger(), toAddress = "to", value = 1.toBigDecimal()))
            .thenReturn(rawTx)
        val credentials = WalletUtils.loadCredentials(password, pathToWallet)
        Mockito.`when`(ethRpc.signTransaction(rawTx, credentials)).thenReturn("signedHex")
        Mockito.`when`(ethRpc.sendTransaction("signedHex")).thenReturn("hash2")

        val latestBlock = Mockito.mock(EthBlock.Block::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(latestBlock)
        Mockito.`when`(latestBlock.number).thenReturn(5.toBigInteger())

        Mockito.`when`(ethRpc.getTransactionByHash("hash2")).thenReturn(ethTx)

        val resTx = ethClient.sendPayment(BigDecimal.ONE, "to")
        Assertions.assertEquals(resTx.status(), TxStatus.VALIDATING)
    }

    @Test
    @DisplayName("constructConfirmedTx test")
    fun constructConfirmedTxTest() {
        val ethTx = mockForConstructConfirmedTx()

        val latestBlock = Mockito.mock(EthBlock.Block::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(latestBlock)
        Mockito.`when`(latestBlock.number).thenReturn(5.toBigInteger())
        Mockito.`when`(latestBlock.numberRaw).thenReturn("0x5")

        val txRt = Mockito.mock(TransactionReceipt::class.java)
        Mockito.`when`(ethRpc.getTransactionReceiptByHash("hash1")).thenReturn(txRt)
        Mockito.`when`(txRt.gasUsed).thenReturn(470.toBigInteger())

        Mockito.`when`(ethRpc.getTransactionByHash("hash1")).thenReturn(ethTx)

        val resultTx = ethClient.constructTx(ethTx)

        Assertions.assertEquals(resultTx.currency(), ethClient.currency)
        Assertions.assertEquals(resultTx.hash(), "hash1")
        Assertions.assertEquals((resultTx.amount()).toString(), "1")
        Assertions.assertEquals(resultTx.destination(), "to")
        Assertions.assertEquals(resultTx.fee(), Convert.fromWei(BigDecimal.valueOf(235000), Convert.Unit.ETHER))
        Assertions.assertEquals(resultTx.status(), TxStatus.CONFIRMED)
    }

    @Test
    @DisplayName("constructValidatingTx test")
    fun constructValidatingTxTest() {
        val ethTx = mockForConstructValidatingTx()

        val latestBlock = Mockito.mock(EthBlock.Block::class.java)
        Mockito.`when`(ethRpc.getLatestBlock()).thenReturn(latestBlock)
        Mockito.`when`(latestBlock.number).thenReturn(5.toBigInteger())


        Mockito.`when`(ethRpc.getTransactionByHash("hash2")).thenReturn(ethTx)

        val resultTx = ethClient.constructTx(ethTx)

        Assertions.assertEquals(resultTx.currency(), ethClient.currency)
        Assertions.assertEquals(resultTx.hash(), "hash2")
        Assertions.assertEquals((resultTx.amount()).toString(), "1")
        Assertions.assertEquals(resultTx.destination(), "to")
        Assertions.assertEquals(resultTx.fee(), Convert.fromWei(BigDecimal.valueOf(450000), Convert.Unit.ETHER))
        Assertions.assertEquals(resultTx.status(), TxStatus.VALIDATING)
    }

    private fun mockForConstructConfirmedTx(): org.web3j.protocol.core.methods.response.Transaction {
        val ethTx = Mockito.mock(org.web3j.protocol.core.methods.response.Transaction::class.java)
        Mockito.`when`(ethTx.blockNumber).thenReturn(1.toBigInteger())
        Mockito.`when`(ethTx.hash).thenReturn("hash1")
        Mockito.`when`(ethTx.to).thenReturn("to")
        Mockito.`when`(ethTx.value).thenReturn(Convert.toWei("1", Convert.Unit.ETHER).toBigInteger()) //1 ether
        Mockito.`when`(ethTx.gas).thenReturn(900.toBigInteger())
        Mockito.`when`(ethTx.gasPrice).thenReturn(500.toBigInteger())
        Mockito.`when`(ethTx.blockHash).thenReturn("blockHash")

        return ethTx
    }

    private fun mockForConstructValidatingTx(): org.web3j.protocol.core.methods.response.Transaction {
        val ethTx = Mockito.mock(org.web3j.protocol.core.methods.response.Transaction::class.java)
        Mockito.`when`(ethTx.blockNumber).thenReturn(4.toBigInteger())
        Mockito.`when`(ethTx.hash).thenReturn("hash2")
        Mockito.`when`(ethTx.to).thenReturn("to")
        Mockito.`when`(ethTx.value).thenReturn(Convert.toWei("1", Convert.Unit.ETHER).toBigInteger()) //1 ether
        Mockito.`when`(ethTx.gas).thenReturn(900.toBigInteger())
        Mockito.`when`(ethTx.gasPrice).thenReturn(500.toBigInteger())

        return ethTx
    }

}