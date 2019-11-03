package dsx.bps.crypto.btc

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.math.roundToInt

@Disabled
internal class BtcRpcTest {

    private val url = "http://bob:password@127.0.0.1:18444/"
    private val rpc = BtcRpc(url)
    private val alice = "2N2PDfbH7zRjB1G7MuiVSYYfykME28wKFw6"

    @Test
    fun getBalance() {
        assertDoesNotThrow {
            val r = rpc.getBalance()
            println("btc getbalance: $r")
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
    fun generate() {
        assertDoesNotThrow {
            val r = rpc.generate(1)
            println("btc generate: {n:1}: $r")
        }

    }
}