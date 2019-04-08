package dsx.bps.crypto.trx

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class TrxRpcTest {

    private val fulURL = "http://34.253.178.165:18190/"
    private val solURL = "http://34.253.178.165:18191/"
    private val trxRpc = TrxRpc(fulURL, solURL)

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
        val num = 23758
        assertDoesNotThrow {
            val block = trxRpc.getBlockByNum(num)
            println(block)
        }
    }

    @Test
    fun getBlockById() {
        val hash = "0000000000005cce2c4b2be92592130c3bb9034c4274a7c9ad882288c9dab7e9"
        assertDoesNotThrow {
            val block = trxRpc.getBlockById(hash)
            println(block)
        }
    }

    @Test
    fun getTransactionById() {
        val hash = "3826fd262cce1c6ac3313d597c468bc927da4d37d6f199513dfaf4797d01a39d"
        assertDoesNotThrow {
            val tx = trxRpc.getTransactionById(hash)
            println(tx)
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
            val tx1 = trxRpc.createTransaction(bob, alice, BigDecimal("678")).also(::println)
            val tx2 = trxRpc.getTransactionSign(alice_p, tx1).also(::println)
            val result = trxRpc.broadcastTransaction(tx2)
            println(result)
        }
    }
}