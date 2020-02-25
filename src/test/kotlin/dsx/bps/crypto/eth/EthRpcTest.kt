package dsx.bps.crypto.eth

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.crypto.WalletUtils
import java.io.File
import java.io.FileFilter
import java.nio.file.Files

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Testcontainers
internal class EthRpcTest {

    private lateinit var url: String
    private lateinit var ethRpc: EthRpc

    private val alice = "0xacfd9f1452e191fa39ff882e5fea428b999fb2af" //eth.accounts[0]
    private val bob = "0x940c955f4072201fd9732bb5000c2d66dec449b6" //eth.accounts[1]
    private val aliceWalletPath =
        "./src/test/resources/ETH/aliceWallet/UTC--2019-11-26T13-14-22.792555600Z--acfd9f1452e191fa39ff882e5fea428b999fb2af"
    private val alice_p = "root"

    private lateinit var address: String
    private var port = 0

    companion object {
        @Container
        @JvmStatic
        val container: KGenericContainer = KGenericContainer("siandreev/ethereum-rpc-test:mining")
            .withExposedPorts(8545, 30303)
    }

    @BeforeEach
    fun setUp() {
        address = container.containerIpAddress
        port = container.firstMappedPort
        url = "http://$address:$port"
        ethRpc = EthRpc(url)
    }

    @Order(1)
    @Test
    fun getBalance() {
        println(address + port)
        assertDoesNotThrow {
            val bal = ethRpc.getBalance(alice)
            println(bal)
        }
    }

    @Order(2)
    @Test
    fun getNewAddress() {
        assertDoesNotThrow {
            val oldFile = lastFileModified("./src/test/resources")
            val address = ethRpc.generateWalletFile("defaultPassword", "./src/test/resources")
            val newFile = lastFileModified("./src/test/resources")
            Assertions.assertNotEquals(oldFile, newFile)
            if (oldFile != newFile && newFile != null) {
                Files.delete(newFile.toPath())
            }
            println(address)
        }
    }

    @Order(3)
    @Test
    fun createRawTransaction() {
        assertDoesNotThrow {
            val tx = ethRpc.createRawTransaction(0.toBigInteger(), toAddress = bob, value = 0.01.toBigDecimal())
            println(tx)
        }
    }

    @Order(4)
    @Test
    fun sendRawTransaction() {
        assertDoesNotThrow {
            val tx = ethRpc.createRawTransaction(0.toBigInteger(), toAddress = bob, value = 0.01.toBigDecimal())
            val credentials = WalletUtils.loadCredentials(alice_p, aliceWalletPath)
            val hash = ethRpc.signTransaction(tx, credentials)
            val result = ethRpc.sendTransaction(hash)
            println(result)
        }
    }

    @Order(5)
    @Test
    fun getTransactionByHash() {
        val tx = ethRpc.createRawTransaction(1.toBigInteger(), toAddress = bob, value = 0.011.toBigDecimal())
        val credentials = WalletUtils.loadCredentials(alice_p, aliceWalletPath)
        val hash = ethRpc.signTransaction(tx, credentials)
        val result = ethRpc.sendTransaction(hash)
        assertDoesNotThrow {
            val trans = ethRpc.getTransactionByHash(result)
            println(trans)
        }
    }

    @Disabled
    @Order(6)
    @Test
    fun sendTransaction() {
        assertDoesNotThrow {
            val pathTOWallet = aliceWalletPath
            val txHash = ethRpc.sendTransaction(pathTOWallet, alice_p, alice, 0.012.toBigDecimal())
            println(txHash)
        }
    }

    @Order(7)
    @Test
    fun getBlockByHash() {
        val hash = ethRpc.getLatestBlock().hash
        assertDoesNotThrow {
            val block = ethRpc.getBlockByHash(hash)
            println(block)
        }
    }

    @Order(8)
    @Test
    fun getLatestBlock() {
        assertDoesNotThrow {
            val block = ethRpc.getLatestBlock()
            println(block.hash)
        }
    }

    @Disabled
    @Order(9)
    @Test
    fun getTransactionReceiptByHash() {
        val tx = ethRpc.createRawTransaction(3.toBigInteger(), toAddress = bob, value = 0.013.toBigDecimal())
        val credentials = WalletUtils.loadCredentials(alice_p, aliceWalletPath)
        val hash = ethRpc.signTransaction(tx, credentials)
        val result = ethRpc.sendTransaction(hash)
        ethRpc.waitForSomeBlocksMining()
        assertDoesNotThrow {
            val txRt = ethRpc.getTransactionReceiptByHash(result)
            println(txRt)
        }
    }

    @Order(10)
    @Test
    fun getTransactionsCount() {
        assertDoesNotThrow {
            val txCnt = ethRpc.getTransactionCount(alice)
            println(txCnt)
        }
    }

    @Order(11)
    @Test
    fun getAllPendingTransactionsCount() {
        assertDoesNotThrow {
            val txCnt = ethRpc.getAllPendingTransactionsCount()
            println(txCnt)
        }
    }

    @Order(12)
    @Test
    fun getPendingTransactionsCount() {
        assertDoesNotThrow {
            val txCnt = ethRpc.getPendingTransactionsCount(alice)
            println(txCnt)
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
}