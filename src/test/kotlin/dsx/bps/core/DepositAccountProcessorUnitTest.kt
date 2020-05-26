package dsx.bps.core

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBclasses.core.DepositAddressEntity
import dsx.bps.DBclasses.core.DepositAddressTable
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.DepositAccountService
import dsx.bps.DBservices.core.TxService
import dsx.bps.TestUtils
import dsx.bps.config.DatabaseConfig
import dsx.bps.config.DepositAccountProcessorConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.File
import java.math.BigDecimal

internal class DepositAccountProcessorUnitTest {
    private val manager: BlockchainPaymentSystemManager = Mockito.mock(BlockchainPaymentSystemManager::class.java)
    private val depositAccountProcessor: DepositAccountProcessor
    private val datasource = Datasource()
    private val depAccService: DepositAccountService
    private val txService: TxService
    private val testConfig: Config

    init {
        val initConfig = Config()
        val configPath = TestUtils.getResourcePath("TestBpsConfig.yaml")
        val configFile = File(configPath)
        testConfig = with(initConfig) {
            addSpec(DepositAccountProcessorConfig)
            from.yaml.file(configFile)
        }
        testConfig.validateRequired()

        val databaseConfig = with(Config()) {
            addSpec(DatabaseConfig)
            from.yaml.file(configFile)
        }
        databaseConfig.validateRequired()

        datasource.initConnection(databaseConfig)
        txService = TxService(datasource)
        depositAccountProcessor = DepositAccountProcessor(manager, testConfig, datasource, txService)
        depAccService = DepositAccountService(datasource)
    }

    @Test
    @DisplayName("create depositAccount and get test")
    fun createDepositAccountTest() {
        val depositAccount = depositAccountProcessor.createNewAccount("id1", listOf(Currency.BTC))
        Assertions.assertNotNull(depAccService.getByDepositId(depositAccount.id))
        Assertions.assertEquals(
            depositAccount.id,
            depAccService.makeDepositAccountFromDB(depAccService.getByDepositId(depositAccount.id)).id
        )
        Assertions.assertNotNull(depositAccountProcessor.getDepositAccount(depositAccount.id))
        Assertions.assertEquals(depositAccount, depositAccountProcessor.getDepositAccount(depositAccount.id))
    }

    @Nested
    inner class OnNextTest {

        @Test
        @DisplayName("onNextTest: right tx")
        fun onNextTest1() {
            val tx = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx.currency()).thenReturn(Currency.BTC)
            Mockito.`when`(tx.destination()).thenReturn("depaddress1")
            Mockito.`when`(tx.paymentReference()).thenReturn("1")
            Mockito.`when`(tx.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx.txid()).thenReturn(TxId("dephash1", 1))

            val depAcc = depositAccountProcessor.createNewAccount("id2", listOf(Currency.BTC))
            depositAccountProcessor.createNewAddress("id2", Currency.BTC, "depaddress1")
            txService.add(
                tx.status(), "depaddress1", "1", BigDecimal.TEN, BigDecimal.ZERO,
                "dephash1", 1, tx.currency()
            )
            depositAccountProcessor.onNext(tx)

            Assertions.assertTrue(depAcc.txids[Currency.BTC]!!.contains(tx.txid()))
            Assertions.assertTrue(transaction {
                DepositAddressEntity.find{ DepositAddressTable.depositAccountId eq depAccService.getByDepositId("id2").id }
                    .first().cryptoAddress == txService.getByTxId("dephash1", 1).cryptoAddress
            })
        }

        @Test
        @DisplayName("onNextTest: wrong tx address")
        fun onNextTest2() {
            val tx = Mockito.mock(Tx::class.java)
            Mockito.`when`(tx.currency()).thenReturn(Currency.BTC)
            Mockito.`when`(tx.destination()).thenReturn("wrongaddress")
            Mockito.`when`(tx.paymentReference()).thenReturn("1")
            Mockito.`when`(tx.amount()).thenReturn(BigDecimal.TEN)
            Mockito.`when`(tx.status()).thenReturn(TxStatus.CONFIRMED)
            Mockito.`when`(tx.txid()).thenReturn(TxId("dephash2", 1))

            val depAcc = depositAccountProcessor.createNewAccount("id3", listOf(Currency.BTC))
            depositAccountProcessor.createNewAddress("id3", Currency.BTC, "depaddress2")
            depositAccountProcessor.onNext(tx)

            Assertions.assertTrue(!depAcc.txids[Currency.BTC]!!.contains(tx.txid()))
        }
    }

    @Test
    @DisplayName("get transactions test")
    fun addAndGetTxTest() {
        val tx1 = Mockito.mock(Tx::class.java)
        Mockito.`when`(tx1.currency()).thenReturn(Currency.BTC)
        Mockito.`when`(tx1.destination()).thenReturn("depaddress3")
        Mockito.`when`(tx1.paymentReference()).thenReturn("3")
        Mockito.`when`(tx1.amount()).thenReturn(BigDecimal.TEN)
        Mockito.`when`(tx1.status()).thenReturn(TxStatus.CONFIRMED)
        Mockito.`when`(tx1.txid()).thenReturn(TxId("dephash3", 3))

        val tx2 = Mockito.mock(Tx::class.java)
        Mockito.`when`(tx2.currency()).thenReturn(Currency.BTC)
        Mockito.`when`(tx2.destination()).thenReturn("depaddress3")
        Mockito.`when`(tx2.paymentReference()).thenReturn("4")
        Mockito.`when`(tx2.amount()).thenReturn(BigDecimal.TEN)
        Mockito.`when`(tx2.status()).thenReturn(TxStatus.CONFIRMED)
        Mockito.`when`(tx2.txid()).thenReturn(TxId("dephash4", 4))

        depositAccountProcessor.createNewAccount("id4", listOf(Currency.BTC))
        depositAccountProcessor.createNewAddress("id4", Currency.BTC, "depaddress3")
        txService.add(
            tx1.status(), "depaddress3", "3", BigDecimal.TEN, BigDecimal.ZERO,
            "dephash3", 3, tx1.currency()
        )
        txService.add(
            tx2.status(), "depaddress3", "4", BigDecimal.TEN, BigDecimal.ZERO,
            "dephash4", 4, tx2.currency()
        )
        depositAccountProcessor.onNext(tx1)
        depositAccountProcessor.onNext(tx2)

        val allTransactions = depositAccountProcessor.getAllTx("id4", Currency.BTC)
        val lastTransaction = depositAccountProcessor.getLastTxToAddress("id4", Currency.BTC, "depaddress3", 1)
        Assertions.assertTrue(
            allTransactions[0].txid() == tx1.txid() &&
                    allTransactions[1].txid() == tx2.txid() &&
                    allTransactions.size == 2
        )
        Assertions.assertTrue(lastTransaction[0].txid() == tx2.txid() && lastTransaction.size == 1)
    }
}