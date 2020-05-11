package dsx.bps.crypto.eth

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.crypto.WalletUtils
import java.io.File
import java.math.BigDecimal
import java.nio.file.Files

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Testcontainers
internal class CommonConnectorTest {

    private lateinit var aliceRpc: CommonConnector
    private lateinit var bobRpc: CommonConnector

    private val aliceAddress = "0x073cfa4b6635b1a1b96f6363a9e499a8076b6107"
    private val bobAddress = "0x0ce59225bcd447feaed698ed754d309feba5fc63"
    private val aliceWalletPath =
        "./src/test/resources/ETH/aliceWallet/" +
                "UTC--2020-03-04T08-33-21.065924100Z--073cfa4b6635b1a1b96f6363a9e499a8076b6107"
    private val bobWalletPath =
        "./src/test/resources/ETH/bobWallet/" +
                "UTC--2020-03-04T08-33-39.016502000Z--0ce59225bcd447feaed698ed754d309feba5fc63"

    private val alicePassword = "password1"
    private val bobPassword = "password2"

    companion object {
        @Container
        @JvmStatic
        val container: KGenericContainer = KGenericContainer("siandreev/ethereum-rpc-test:PoA-mining")
            .withExposedPorts(8541, 8542)
            .waitingFor(
                Wait.forLogMessage(".*The node is ready!.*", 1)
            )
    }

    @BeforeAll
    fun setUp() {
        val address = container.containerIpAddress

        val alicePort = container.getMappedPort(8541)
        val aliceUrl = "http://$address:$alicePort"
        aliceRpc = CommonConnector(aliceUrl)

        val bobPort = container.getMappedPort(8542)
        val bobUrl = "http://$address:$bobPort"
        bobRpc = CommonConnector(bobUrl)
    }

    @Order(1)
    @Test
    fun getBalance() {
        assertDoesNotThrow {
            val bal = aliceRpc.getBalance(aliceAddress)
            val realBalance = BigDecimal("50")
            assertEquals(realBalance, bal)
        }
    }

    @Order(2)
    @Test
    fun getNewAddressWithWallet() {
        assertDoesNotThrow {
            val oldFile = lastFileModified()
            aliceRpc.generateWalletFile("defaultPassword", "./src/test/resources")
            val newFile = lastFileModified()
            assertNotEquals(oldFile, newFile)
            if (oldFile != newFile && newFile != null) {
                Files.delete(newFile.toPath())
            }
        }
    }

    @Order(3)
    @Test
    fun createRawTransaction() {
        assertDoesNotThrow {
            aliceRpc.createRawTransaction(0.toBigInteger(), toAddress = bobAddress, value = 0.01.toBigDecimal())
        }
    }

    @Order(4)
    @Test
    fun sendRawTransaction() {
        assertDoesNotThrow {
            val tx = aliceRpc.createRawTransaction(0.toBigInteger(), toAddress = bobAddress, value = 0.01.toBigDecimal())
            val credentials = WalletUtils.loadCredentials(alicePassword, aliceWalletPath)
            val hash = aliceRpc.signTransaction(tx, credentials)
            aliceRpc.sendTransaction(hash)
        }
    }

    @Order(5)
    @Test
    fun getTransactionByHash() {
        val tx = aliceRpc.createRawTransaction(1.toBigInteger(), toAddress = bobAddress, value = 0.011.toBigDecimal())
        val credentials = WalletUtils.loadCredentials(alicePassword, aliceWalletPath)
        val hash = aliceRpc.signTransaction(tx, credentials)
        val result = aliceRpc.sendTransaction(hash)
        assertDoesNotThrow {
            aliceRpc.getTransactionByHash(result)
        }
    }

    @Order(6)
    @Test
    fun sendTransaction() {
        assertDoesNotThrow {
            val pathTOWallet = aliceWalletPath
            aliceRpc.sendTransaction(pathTOWallet, alicePassword, aliceAddress, 0.012.toBigDecimal())
        }
    }

    @Order(7)
    @Test
    fun getBlockByHash() {
        val hash = aliceRpc.getLatestBlock().hash
        assertDoesNotThrow {
            aliceRpc.getBlockByHash(hash)
        }
    }

    @Order(8)
    @Test
    fun getLatestBlock() {
        assertDoesNotThrow {
            aliceRpc.getLatestBlock()
        }
    }

    @Order(9)
    @Test
    fun getTransactionReceiptByHash() {
        val tx = aliceRpc.createRawTransaction(3.toBigInteger(), toAddress = bobAddress, value = 0.013.toBigDecimal())
        val credentials = WalletUtils.loadCredentials(alicePassword, aliceWalletPath)
        val hash = aliceRpc.signTransaction(tx, credentials)
        val result = aliceRpc.sendTransaction(hash)
        waitForSomeBlocksMining()
        waitForSomeBlocksMining()
        assertDoesNotThrow {
            aliceRpc.getTransactionReceiptByHash(result)
        }
    }

    @Order(10)
    @Test
    fun getTransactionsCount() {
        assertDoesNotThrow {
            aliceRpc.getTransactionCount(aliceAddress)
        }
    }

    @Order(11)
    @Test
    fun getAllPendingTransactionsCount() {
        assertDoesNotThrow {
            aliceRpc.getAllPendingTransactionsCount()
        }
    }

    @Order(12)
    @Test
    fun getPendingTransactionsCount() {
        assertDoesNotThrow {
            aliceRpc.getPendingTransactionsCount(aliceAddress)
        }
    }

    @Disabled
    @Order(13)
    @Test
    fun createSmartContractAndCheckMoneyRouting() {
        assertDoesNotThrow {
            val contract = aliceRpc.generateSmartWallet(aliceWalletPath, alicePassword)
            val aliceBalanceBefore = aliceRpc.getBalance(aliceAddress)

            val tx = bobRpc.createRawTransaction(0.toBigInteger(), toAddress = contract.address,
                value = 15.toBigDecimal())
            val credentials = WalletUtils.loadCredentials(bobPassword, bobWalletPath)
            bobRpc.sendTransaction(bobRpc.signTransaction(tx, credentials))

            waitForSomeBlocksMining()
            waitForSomeBlocksMining()

            val aliceBalanceAfter = aliceRpc.getBalance(aliceAddress)
            assertTrue(aliceBalanceAfter >= aliceBalanceBefore.plus(BigDecimal.valueOf(15)))
        }
    }

    private fun lastFileModified(): File? {
        val dir = "./src/test/resources"
        val fl = File(dir)
        val files = fl.listFiles { file -> file.isFile }
        var lastMod = java.lang.Long.MIN_VALUE
        var choice: File? = null
        for (file in files!!) {
            if (file.lastModified() > lastMod) {
                choice = file
                lastMod = file.lastModified()
            }
        }
        return choice
    }

    private fun waitForSomeBlocksMining() {
        val latestHash = aliceRpc.getLatestBlock()
        var count = 0
        while (aliceRpc.getLatestBlock() == latestHash && count < 160) {
            Thread.sleep(2000)
            count++
        }
        if (count >= 160) {
            throw Exception("Block mining timed out")
        }
    }
}
