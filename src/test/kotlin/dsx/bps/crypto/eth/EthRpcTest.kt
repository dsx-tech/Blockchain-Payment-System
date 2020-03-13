package dsx.bps.crypto.eth

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.crypto.WalletUtils
import java.io.File
import java.io.FileFilter
import java.math.BigDecimal
import java.nio.file.Files

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Testcontainers
internal class EthRpcTest {

    private lateinit var url: String
    private lateinit var ethRpc: EthRpc

    private val alice = "0x073cfa4b6635b1a1b96f6363a9e499a8076b6107"
    private val bob = "0x0ce59225bcd447feaed698ed754d309feba5fc63"
    private val aliceWalletPath =
        "./src/test/resources/ETH/aliceWallet/UTC--2020-03-04T08-33-21.065924100Z--073cfa4b6635b1a1b96f6363a9e499a8076b6107"
    private val alice_p = "password1"
    private lateinit var address: String
    private var port = 0

    companion object {
        @Container
        @JvmStatic
        val container: KGenericContainer = KGenericContainer("siandreev/ethereum-rpc-test:PoA-mining")
            .withExposedPorts(8541, 8542)
    }

    @BeforeEach
    fun setUp() {
        address = container.containerIpAddress
        port = container.firstMappedPort
        url = "http://$address:${port}"
        ethRpc = EthRpc(url)
    }

    @Order(1)
    @Test
    fun getBalance() {
        assertDoesNotThrow {
            val bal = ethRpc.getBalance(alice)
            val realBalance = BigDecimal("904625697166532776746648320380374280103671755200316906558.261906867821325312")
            assertEquals(realBalance, bal)
        }
    }

    @Order(2)
    @Test
    fun getNewAddress() {
        assertDoesNotThrow {
            val oldFile = lastFileModified("./src/test/resources")
            ethRpc.generateWalletFile("defaultPassword", "./src/test/resources")
            val newFile = lastFileModified("./src/test/resources")
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
            ethRpc.createRawTransaction(0.toBigInteger(), toAddress = bob, value = 0.01.toBigDecimal())
        }
    }

    @Order(4)
    @Test
    fun sendRawTransaction() {
        assertDoesNotThrow {
            val tx = ethRpc.createRawTransaction(3.toBigInteger(), toAddress = bob, value = 0.01.toBigDecimal())
            val credentials = WalletUtils.loadCredentials(alice_p, aliceWalletPath)
            val hash = ethRpc.signTransaction(tx, credentials)
            ethRpc.sendTransaction(hash)
        }
    }

    @Order(5)
    @Test
    fun getTransactionByHash() {
        val tx = ethRpc.createRawTransaction(4.toBigInteger(), toAddress = bob, value = 0.011.toBigDecimal())
        val credentials = WalletUtils.loadCredentials(alice_p, aliceWalletPath)
        val hash = ethRpc.signTransaction(tx, credentials)
        val result = ethRpc.sendTransaction(hash)
        assertDoesNotThrow {
            ethRpc.getTransactionByHash(result)
        }
    }

    @Order(6)
    @Test
    fun sendTransaction() {
        assertDoesNotThrow {
            val pathTOWallet = aliceWalletPath
            ethRpc.sendTransaction(pathTOWallet, alice_p, alice, 0.012.toBigDecimal())
        }
    }

    @Order(7)
    @Test
    fun getBlockByHash() {
        val hash = ethRpc.getLatestBlock().hash
        assertDoesNotThrow {
            ethRpc.getBlockByHash(hash)
        }
    }

    @Order(8)
    @Test
    fun getLatestBlock() {
        assertDoesNotThrow {
            ethRpc.getLatestBlock()
        }
    }

    @Order(9)
    @Test
    fun getTransactionReceiptByHash() {
        val tx = ethRpc.createRawTransaction(5.toBigInteger(), toAddress = bob, value = 0.013.toBigDecimal())
        val credentials = WalletUtils.loadCredentials(alice_p, aliceWalletPath)
        val hash = ethRpc.signTransaction(tx, credentials)
        val result = ethRpc.sendTransaction(hash)
        waitForSomeBlocksMining()
        assertDoesNotThrow {
            ethRpc.getTransactionReceiptByHash(result)
        }
    }

    @Order(10)
    @Test
    fun getTransactionsCount() {
        assertDoesNotThrow {
            ethRpc.getTransactionCount(alice)
        }
    }

    @Order(11)
    @Test
    fun getAllPendingTransactionsCount() {
        assertDoesNotThrow {
            ethRpc.getAllPendingTransactionsCount()
        }
    }

    @Order(12)
    @Test
    fun getPendingTransactionsCount() {
        assertDoesNotThrow {
            ethRpc.getPendingTransactionsCount(alice)
        }
    }

    fun lastFileModified(dir: String): File? {
        val fl = File(dir)
        val files = fl.listFiles(object: FileFilter {
            override fun accept(file: File): Boolean {
                return file.isFile
            }
        })
        var lastMod = java.lang.Long.MIN_VALUE
        var choice: File? = null
        for (file in files) {
            if (file.lastModified() > lastMod) {
                choice = file
                lastMod = file.lastModified()
            }
        }
        return choice
    }

    fun waitForSomeBlocksMining() {
        val latestHash = ethRpc.getLatestBlock()
        var count = 0
        while (ethRpc.getLatestBlock() == latestHash && count < 160) {
            Thread.sleep(2000)
            count++
        }
        if (count >= 160) {
            throw Exception("Block mining timed out")
        }
    }
}