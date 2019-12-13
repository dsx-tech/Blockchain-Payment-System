package dsx.bps.crypto.eth


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.web3j.crypto.WalletUtils
import java.io.File
import java.io.FileFilter

@Disabled
internal class EthRpcTest {

    private val url = "http://127.0.0.1:8545"
    private val ethRpc = EthRpc(url)

    private val alice = "0xacfd9f1452e191fa39ff882e5fea428b999fb2af" //eth.accounts[0]
    private val bob = "0x940c955f4072201fd9732bb5000c2d66dec449b6" //eth.accounts[1]
    private val alice_p = "root"
    private val bob_p = "test"

    @Test
    fun getBalance() {
        assertDoesNotThrow {
            val bal = ethRpc.getBalance(alice)
            println(bal)
        }
    }

    @Test
    fun getNewAddress() {
        assertDoesNotThrow {
            val oldFile = lastFileModified("./src/test/resources")
            val address = ethRpc.generateWalletFile("defaultPassword",".src/test/resources")
            val newFile = lastFileModified("./src/test/resources")
            Assertions.assertNotEquals(oldFile, newFile)
            println(address)
        }
    }

    @Test
    fun createRawTransaction() {
        assertDoesNotThrow {
            val tx = ethRpc.createRawTransaction(0.toBigInteger(), toAddress = bob,value = 0.1.toBigDecimal())
            println(tx)
        }
    }

    @Test
    fun sendRawTRansaction(){
        assertDoesNotThrow{
            val tx = ethRpc.createRawTransaction(4.toBigInteger(), toAddress = bob,value = 0.1.toBigDecimal())
            val credentials = WalletUtils.loadCredentials("defaultPassword",
                "C:\\Users\\Admin\\Documents\\Programming\\DSXT\\ETH\\datadir\\test-dev-03-01\\keystore\\UTC--2019-12-13T10-29-08.310000000Z--9d539f5bd0323455bb9c60f10a1c2ca637141dc4.json")
            val hash = ethRpc.signTransaction(tx, credentials)
            val result = ethRpc.sendTransaction(hash)
            println(result)
        }
    }

    @Test
    fun getTransactionByHash() {
        val hash = "0xa6d7d1bea0e8fd13af7b72199575d9b6d80af1aacc1c6eb02be7c7cfade34133"
        assertDoesNotThrow {
            val tx = ethRpc.getTransactionByHash(hash)
            println(tx)
        }
    }

    @Test
    fun sendTransaction() {  // autocheck tx status
        assertDoesNotThrow { // need to miner.start(1); admin.sleepBlocks(1); miner.stop() in geth
            val pathTOWallet = "C:\\Users\\Admin\\Documents\\Programming\\DSXT\\ETH\\datadir\\test-dev-03-01\\keystore\\UTC--2019-12-13T10-29-08.310000000Z--9d539f5bd0323455bb9c60f10a1c2ca637141dc4.json"
            val txHash = ethRpc.sendTransaction(pathTOWallet, "defaultPassword", alice, 0.1.toBigDecimal())
            println(txHash)
        }
    }

    @Test
    fun getBlockByHash() {
        val hash = "0x3df2ed8f903bd10d4129d56404c7725399cb68cde825e7a09ebc4c6969f4c563"
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

    @Test
    fun getTransactionReceiptByHash() {
        val hash = "0xa6d7d1bea0e8fd13af7b72199575d9b6d80af1aacc1c6eb02be7c7cfade34133"
        assertDoesNotThrow {
            val txRt = ethRpc.getTransactionReceiptByHash(hash)
            println(txRt)
        }
    }

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

}