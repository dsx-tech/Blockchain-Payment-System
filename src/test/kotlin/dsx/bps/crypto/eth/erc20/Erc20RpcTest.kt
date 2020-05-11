package dsx.bps.crypto.eth.erc20

import dsx.bps.crypto.eth.CommonConnector
import dsx.bps.crypto.eth.KGenericContainer
import org.junit.jupiter.api.*
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.crypto.WalletUtils
import java.math.BigInteger

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Testcontainers
internal class Erc20RpcTest {

    private lateinit var aliceRpc: Erc20Rpc
    private lateinit var bobRpc: Erc20Rpc

    private val contractAddress = "0x56bc568b19d37b5742f60ac3f4c56a3b3d266aee"
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

        val aliceCredentials = WalletUtils.loadCredentials(alicePassword, aliceWalletPath)
        val aliceCommonConnector = CommonConnector(aliceUrl)
        aliceRpc = Erc20Rpc(aliceCommonConnector, contractAddress, aliceCredentials)

        val bobPort = container.getMappedPort(8542)
        val bobUrl = "http://$address:$bobPort"

        val bobCredentials = WalletUtils.loadCredentials(bobPassword, bobWalletPath)
        val bobCommonConnector = CommonConnector(bobUrl)
        bobRpc = Erc20Rpc(bobCommonConnector, contractAddress, bobCredentials)
    }

    @Order(1)
    @Test
    fun getBalance() {
        Assertions.assertDoesNotThrow {
            val aliceBalance = aliceRpc.balanceOf(aliceAddress)
            val realAliceBalance = BigInteger("1000000000000000000000") // 1000 tokens
            Assertions.assertEquals(realAliceBalance, aliceBalance)

            val bobBalance = bobRpc.balanceOf(bobAddress)
            val realBobBalance = BigInteger("200000000000000000000") // 200 tokens
            Assertions.assertEquals(realBobBalance, bobBalance)
        }
    }

    @Order(2)
    @Test
    fun sendTransaction() {
        Assertions.assertDoesNotThrow {
            val amount = BigInteger("10")
            val bobBalance1 = bobRpc.balanceOf(bobAddress)
            aliceRpc.transfer(bobAddress, amount)

            waitForSomeBlocksMining()

            val bobBalance2 = bobRpc.balanceOf(bobAddress)
            Assertions.assertEquals(bobBalance2, bobBalance1 + amount)

        }
    }

    private fun waitForSomeBlocksMining() {
        val latestHash = aliceRpc.ethRpc.getLatestBlock()
        var count = 0
        while (aliceRpc.ethRpc.getLatestBlock() == latestHash && count < 160) {
            Thread.sleep(2000)
            count++
        }
        if (count >= 160) {
            throw Exception("Block mining timed out")
        }
    }
}