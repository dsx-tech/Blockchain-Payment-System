package dsx.bps.crypto.btc

import dsx.bps.crypto.eth.KGenericContainer
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import kotlin.math.roundToInt

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Testcontainers
internal class BtcRpcTest {

    private lateinit var url: String
    private lateinit var rpc : BtcRpc
    private val alice = "2N2PDfbH7zRjB1G7MuiVSYYfykME28wKFw6"

    companion object {
        @Container
        @JvmStatic
        val container: KGenericContainer = KGenericContainer("siandreev/bitcoind-regtest:alice-bob-regtest")
            .withExposedPorts(18443, 18444)
            .waitingFor(
                Wait.forLogMessage(".*The node is ready!.*", 1))

    }

    @BeforeEach
    fun setUp() {
        val address = container.containerIpAddress
        val port = container.firstMappedPort
        url = "http://alice:password@$address:$port/"
        rpc = BtcRpc(url)
    }

    @Order(1)
    @Test
    fun getBalance() {
        assertDoesNotThrow {
            val r = rpc.getBalance()
            assertEquals(5050.0.toBigDecimal(), r)
        }
    }

    @Test
    fun getNewAddress() {
        assertDoesNotThrow {
            val r = rpc.getNewAddress()
            println("btc getnewaddress: $r")
        }
    }

    @Test
    fun getBestBlockHash() {
        assertDoesNotThrow {
            val r = rpc.getBestBlockHash()
            println("btc getbestblockhash: $r")
        }
    }

    @Test
    fun createRawTransaction() {
        val amount = BigDecimal("0.0123")
        assertDoesNotThrow {
            val r = rpc.createRawTransaction(amount, alice)
            println("btc createrawtransaction {amount:$amount, address:$alice}: $r")
        }
    }

    @Test
    fun fundRawTransaction() {
        val amount = BigDecimal("0.0123")
        assertDoesNotThrow {
            val r1 = rpc.createRawTransaction(amount, alice)
            val r2 = rpc.fundRawTransaction(r1)
            println("btc fundrawtransaction {hex:$r1}: $r2")
        }
    }

    @Test
    fun signRawTransactionWithWallet() {
        val amount = BigDecimal("0.0123")
        assertDoesNotThrow {
            val r1 = rpc.createRawTransaction(amount, alice)
            val r2 = rpc.fundRawTransaction(r1)
            val r3 = rpc.signRawTransactionWithWallet(r2)
            println("btc signrawtransactionwithwallet {hex:$r2}: $r3")
        }
    }

    @Test
    fun sendRawTransaction() {
        val amount = BigDecimal("0.0123")
        assertDoesNotThrow {
            val r1 = rpc.createRawTransaction(amount, alice)
            val r2 = rpc.fundRawTransaction(r1)
            val r3 = rpc.signRawTransactionWithWallet(r2)
            val r4 = rpc.sendRawTransaction(r3)
            println("btc sendrawtransaction {hex:$r3}: $r4")
        }
    }

    @Test
    fun getBlock() {
        assertDoesNotThrow {
            val r1 = rpc.getBestBlockHash()
            val r2 = rpc.getBlock(r1)
            println("btc getblock {hash:$r1}: $r2")
        }
    }

    @Test
    fun getBlockCount() {
        assertDoesNotThrow {
            val r = rpc.query("getblockcount").toString()
            println("btc getblockcount: $r")
        }
    }

    @Test
    fun getBlockHash() {
        assertDoesNotThrow {
            val r1 = rpc.query("getblockcount").toString()
            val r2 = rpc.query("getblockhash", r1.toDouble().roundToInt())
            println("btc getblockhash: {height:$r1}: $r2")
        }
    }

    @Test
    fun listSinceBlock() {
        assertDoesNotThrow {
            val r1 = rpc.query("getblockcount").toString()
            val r2 = rpc.query("getblockhash", (r1.toDouble() - 2).roundToInt()) as String
            val r3 = rpc.listSinceBlock(r2)
            println("btc listsinceblock: {hash:$r2}: $r3")
        }
    }

    @Test
    fun getTransaction() {
        val amount = BigDecimal("0.0123")
        assertDoesNotThrow {
            val r1 = rpc.createRawTransaction(amount, alice)
            val r2 = rpc.fundRawTransaction(r1)
            val r3 = rpc.signRawTransactionWithWallet(r2)
            val r4 = rpc.sendRawTransaction(r3)
            val r5 = rpc.getTransaction(r4)
            println("btc gettransaction {hash:$r4}: $r5")
        }
    }

    @Test
    fun generatetoaddress() {
        assertDoesNotThrow {
            val r = rpc.generatetoaddress(1, alice)
            println("btc generate: {n:1}: $r")
        }
    }
}