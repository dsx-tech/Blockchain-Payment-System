package dsx.bps.crypto.trx

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class TrxRpcTest {

    private val url = "http://34.253.178.165:18190/wallet/"
    private val trxRpc = TrxRpc(url)

    private val alice = "41dc6c2bc46639e5371fe99e002858754604cf5847"
    private val alice_p = "92f451c194301b4a1eae3a818cc181e1a319656dcdf6760cdbe35c54b05bb3ec"
    private val bob = "4134b8aa365f035fe905bf45344f249bea71ccf19a"

    @Test
    fun getBalance() {
        assertDoesNotThrow {
            val bal = trxRpc.getBalance(alice)
            println(bal)
        }
    }

    @Test
    fun getAccount() {
        assertDoesNotThrow {
            val acc = trxRpc.getAccount(alice)
            println(acc)
        }
    }

    @Test
    fun getNowBlock() {
        assertDoesNotThrow {
            val block = trxRpc.getNowBlock()
            println(block)
        }
    }

    @Test
    fun getBlockByNum() {
        val num = 434
        assertDoesNotThrow {
            val block = trxRpc.getBlockByNum(num)
            println(block)
        }
    }

    @Test
    fun getBlockById() {
        val hash = "0000000000000146e414afe7964eb30deceb8d3921f60287a20070c6d94c7152"
        assertDoesNotThrow {
            val block = trxRpc.getBlockById(hash)
            println(block)
        }
    }

    @Test
    fun getTransactionById() {
        val hash = "a05663ef0bce4546bb78cbd72b190b74c6d05e9f35a52f8c7c8b150a1a4581ac"
        assertDoesNotThrow {
            val tx = trxRpc.getTransactionById(hash)
            println(tx)
        }
    }

    @Test
    fun getTransactionInfoById() {
        val hash = "a05663ef0bce4546bb78cbd72b190b74c6d05e9f35a52f8c7c8b150a1a4581ac"
        assertDoesNotThrow {
            val txInfo = trxRpc.getTransactionInfoById(hash)
            println(txInfo)
        }
    }

    @Test
    fun createTransaction() {
        assertDoesNotThrow {
            val tx = trxRpc.createTransaction(bob, alice, BigDecimal("456"))
            println(tx)
        }
    }

    @Test
    fun getTransactionSign() {
        assertDoesNotThrow {
            val tx1 = trxRpc.createTransaction(bob, alice, BigDecimal("567")).also(::println)
            val tx2 = trxRpc.getTransactionSign(alice_p, tx1)
            println(tx2)
        }
    }

    @Test
    fun broadcastTransaction() {
        assertDoesNotThrow {
            val tx1 = trxRpc.createTransaction(bob, alice, BigDecimal("353")).also(::println)
            val tx2 = trxRpc.getTransactionSign(alice_p, tx1).also(::println)
            val result = trxRpc.broadcastTransaction(tx2)
            println(result)
        }
    }
}