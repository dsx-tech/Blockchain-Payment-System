package dsx.bps.crypto.eth


import com.github.dockerjava.api.model.ExposedPort
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.crypto.WalletUtils
import java.io.File
import java.io.FileFilter
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.wait.strategy.Wait
import java.nio.file.Files


class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

@Testcontainers
internal class EthRpcTest {

    private var url = "http://127.0.0.1:8545"
    private var ethRpc = EthRpc(url)

    private val alice = "0xacfd9f1452e191fa39ff882e5fea428b999fb2af" //eth.accounts[0]
    private val bob = "0x940c955f4072201fd9732bb5000c2d66dec449b6" //eth.accounts[1]
    private val aliceWalletPath = "./src/test/resources/keystore/UTC--2019-11-26T13-14-22.792555600Z--acfd9f1452e191fa39ff882e5fea428b999fb2af"
    private val alice_p = "root"

    private var address = ""
    private var port = ""


    @Container
    var redis = KGenericContainer("siandreev/ethereum-rpc-test:mining")
        .withExposedPorts(8545, 30303)

    @BeforeEach
    fun setUp() {
        val address = redis.containerIpAddress
        val port = redis.firstMappedPort
        url = "http://$address:$port"
        ethRpc = EthRpc(url)
    }

    @Test
    fun getBalance() {
        println(address + port)
        assertDoesNotThrow {
            val bal = ethRpc.getBalance(alice)
            println(bal)
        }
    }

    @Test
    fun getNewAddress() {
        assertDoesNotThrow {
            val oldFile = lastFileModified("./src/test/resources")
            val address = ethRpc.generateWalletFile("defaultPassword","./src/test/resources")
            val newFile = lastFileModified("./src/test/resources")
            Assertions.assertNotEquals(oldFile, newFile)
            if (oldFile != newFile && newFile != null)
            {
                Files.delete(newFile.toPath())
            }
            println(address)
        }
    }

    @Test
    fun createRawTransaction() {
        assertDoesNotThrow {
            val tx = ethRpc.createRawTransaction(0.toBigInteger(), toAddress = bob,value = 0.01.toBigDecimal())
            println(tx)
        }
    }

    @Test
    fun sendRawTransaction(){
        assertDoesNotThrow{
            val result = sendRawTx()
            println(result)
        }
    }

    @Test
    fun getTransactionByHash() {
        val hash = sendRawTx()
        assertDoesNotThrow {
            val tx = ethRpc.getTransactionByHash(hash)
            println(tx)
        }
    }

   /* @Test
    fun sendTransaction() {  // autocheck tx status
        assertDoesNotThrow { // need to miner.start(1); admin.sleepBlocks(1); miner.stop() in geth
            val pathTOWallet = aliceWalletPath
            val txHash = ethRpc.sendTransaction(pathTOWallet, alice_p, alice, 0.1.toBigDecimal())
            println(txHash)
        }
    } */

    @Test
    fun getBlockByHash() {
        val hash = ethRpc.getLatestBlock().hash
        assertDoesNotThrow {
            val block = ethRpc.getBlockByHash(hash)
            println(block)
        }
    }

    @Test
    fun getLatestBlock() {
        assertDoesNotThrow {
            val block = ethRpc.getLatestBlock()
                println(block.hash)
        }
    }

    /* @Test //TODO: Tests work, but it takes about 6 min; need to reduce working time
    fun getTransactionReceiptByHash() {
        val block = ethRpc.getLatestBlock()
        val hash = sendRawTx()
        while (block == ethRpc.getLatestBlock())
        {
            Thread.sleep(10000)
        }
        assertDoesNotThrow {
            val txRt = ethRpc.getTransactionReceiptByHash(hash)
            println(txRt)
        }
    } */

    @Test
    fun getTransactionsCount() {
        assertDoesNotThrow {
            val txCnt = ethRpc.getTransactionCount(alice)
            println(txCnt)
        }
    }

    @Test
    fun getAllPendingTransactionsCount() {
        assertDoesNotThrow {
            val txCnt = ethRpc.getAllPendingTransactionsCount()
            println(txCnt)
        }
    }

    @Test
    fun getPendingTransactionsCount() {
        assertDoesNotThrow {
            val txCnt = ethRpc.getPendingTransactionsCount(alice)
            println(txCnt)
        }
    }

    fun lastFileModified(dir: String): File? {
        val fl = File(dir)
        val files = fl.listFiles(object : FileFilter {
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

    fun sendRawTx() : String{
        val tx = ethRpc.createRawTransaction(4.toBigInteger(), toAddress = bob,value = 0.01.toBigDecimal())
        val credentials = WalletUtils.loadCredentials(alice_p, aliceWalletPath)
        val hash = ethRpc.signTransaction(tx, credentials)
        val result = ethRpc.sendTransaction(hash)
        return result
    }

}