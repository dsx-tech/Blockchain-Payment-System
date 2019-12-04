package dsx.bps.crypto.eth


import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

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
    fun getTransactionByHash() {
        val hash = "0x2752f62aa49cc0df8f67f3e61709b9762b668aecba6cf6e79f7266af46b75da6"
        assertDoesNotThrow {
            val tx = ethRpc.getTransactionByHash(hash)
            println(tx)
        }
    }

    @Test
    fun getBlockByHash() {
        val hash = "0x557520108cf59cbe8bd0721e9c6ad0088e60c14fbe67c3e57a097432c409e5a0"
        assertDoesNotThrow {
            val block = ethRpc.getBlockByHash(hash)
            println(block)
        }
    }

    @Test
    fun getLatestBlock() {
        assertDoesNotThrow {
            val block = ethRpc.getLatestBlock()
            println(block)
        }
    }

    @Test
    fun getTransactionReceipt() {
        val hash = "0x2752f62aa49cc0df8f67f3e61709b9762b668aecba6cf6e79f7266af46b75da6"
        assertDoesNotThrow {
            val txRt = ethRpc.getTransactionReceipt(hash)
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
    fun unlockAccount() {
        assertDoesNotThrow {
            val result = ethRpc.unlockAccount(alice, alice_p)
            println(result)
        }
    }

    @Test
    fun sendTransaction() {
        unlockAccount()
        assertDoesNotThrow {
            val txHash = ethRpc.sendTransaction(__from = alice, __to = bob, __value = 1.toBigDecimal())
            println(txHash)
        }
    }
}