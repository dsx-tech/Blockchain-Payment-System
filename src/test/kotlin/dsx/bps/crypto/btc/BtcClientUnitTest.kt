package dsx.bps.crypto.btc

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.TxService
import dsx.bps.config.DatabaseConfig
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.btc.datamodel.BtcTx
import dsx.bps.crypto.btc.datamodel.BtcTxDetail
import dsx.bps.crypto.btc.datamodel.BtcTxSinceBlock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.File
import java.math.BigDecimal

internal class BtcClientUnitTest {

    private val btcRpc = Mockito.mock(BtcRpc::class.java)
    private val btcExplorer = Mockito.mock(BtcExplorer::class.java)
    private val datasource = Datasource()
    private val btcCoin: BtcCoin
    private val txService: TxService

    init {
        val configFile = File(javaClass.getResource("/TestBpsConfig.yaml").path)
        val databaseConfig = with(Config()) {
            addSpec(DatabaseConfig)
            from.yaml.file(configFile)
        }
        databaseConfig.validateRequired()

        datasource.initConnection(databaseConfig)
        txService = TxService(datasource)
        btcCoin = BtcCoin(
            btcRpc, btcExplorer,
            javaClass.getResource("/TestBpsConfig.yaml").path,
            datasource, txService
        )
    }

    @Test
    @DisplayName("getBalance test ")
    fun getBalanceTest() {
        btcCoin.getBalance()
        Mockito.verify(btcRpc, Mockito.only()).getBalance()
    }

    @Test
    @DisplayName("getAddress test")
    fun getAddressTest() {
        btcCoin.getAddress()
        Mockito.verify(btcRpc, Mockito.only()).getNewAddress()
    }

    @Test
    @DisplayName("getTag test")
    fun getTagTest() {
        Assertions.assertEquals(btcCoin.getTag(), null)
    }

    @Test
    @DisplayName("getTx test")
    fun getTxTest() {
        val btcTxDetail: BtcTxDetail = Mockito.mock(BtcTxDetail::class.java)
        Mockito.`when`(btcTxDetail.vout).thenReturn(1)
        val btcTx: BtcTx = Mockito.mock(BtcTx::class.java)
        Mockito.`when`(btcTx.details).thenReturn(listOf(btcTxDetail))

        Mockito.`when`(btcRpc.getTransaction("hash")).thenReturn(btcTx)

        val txid: TxId = Mockito.mock(TxId::class.java)
        Mockito.`when`(txid.hash).thenReturn("hash")
        Mockito.`when`(txid.index).thenReturn(1)

        btcCoin.getTx(txid)
    }

    @Test
    @DisplayName("sendPayment test")
    fun sendPaymentTest() {
        val btcTxDetail: BtcTxDetail = Mockito.mock(BtcTxDetail::class.java)
        Mockito.`when`(btcTxDetail.vout).thenReturn(1)
        Mockito.`when`(btcTxDetail.category).thenReturn("send")
        Mockito.`when`(btcTxDetail.address).thenReturn("testaddress")
        Mockito.`when`(btcTxDetail.amount).thenReturn(BigDecimal.TEN)
        Mockito.`when`(btcTxDetail.fee).thenReturn(BigDecimal.ZERO)

        val btcTx: BtcTx = Mockito.mock(BtcTx::class.java)
        Mockito.`when`(btcTx.details).thenReturn(listOf(btcTxDetail))
        Mockito.`when`(btcTx.hash).thenReturn("hash")

        Mockito.`when`(btcRpc.getTransaction("hash")).thenReturn(btcTx)
        Mockito.`when`(btcRpc.createRawTransaction(BigDecimal.TEN, "testaddress")).thenReturn("rawTx")
        Mockito.`when`(btcRpc.fundRawTransaction("rawTx")).thenReturn("fundRawTx")
        Mockito.`when`(btcRpc.signRawTransactionWithWallet("fundRawTx")).thenReturn("signFundRawTx")
        Mockito.`when`(btcRpc.sendRawTransaction("signFundRawTx")).thenReturn("hash")
        Mockito.`when`(btcRpc.getTransaction("hash")).thenReturn(btcTx)

        val result = btcCoin.sendPayment(BigDecimal.TEN, "testaddress", null)
        Assertions.assertEquals(result.currency(), txService.getByTxId("hash", 1).currency)
        Assertions.assertEquals(result.destination(), txService.getByTxId("hash", 1).destination)
        Assertions.assertEquals(result.index(), txService.getByTxId("hash", 1).index)
        Assertions.assertEquals(result.hash(), txService.getByTxId("hash", 1).hash)
        Assertions.assertEquals(
            result.amount(),
            txService.getByTxId("hash", 1).amount.stripTrailingZeros().add(BigDecimal.ZERO)
        )
        Assertions.assertEquals(result.currency(), btcCoin.currency)
        Assertions.assertEquals(result.destination(), btcTxDetail.address)
        Assertions.assertEquals(result.index(), btcTxDetail.vout)
        Assertions.assertEquals(result.hash(), btcTx.hash)
        Assertions.assertEquals(result.amount(), BigDecimal.TEN)
    }

    @Test
    @DisplayName("getBestBlockHash test")
    fun getBestBlockHashTest() {
        btcCoin.getBestBlockHash()
        Mockito.verify(btcRpc, Mockito.only()).getBestBlockHash()
    }

    @Test
    @DisplayName("getBlock test")
    fun getBlockTest() {
        btcCoin.getBlock("hash")
        Mockito.verify(btcRpc, Mockito.only()).getBlock("hash")
    }

    @Test
    @DisplayName("listSinceBlock test")
    fun listSinceBlockTest() {
        btcCoin.listSinceBlock("hash")
        Mockito.verify(btcRpc, Mockito.only()).listSinceBlock("hash")
    }

    @Test
    @DisplayName("constructTx(btcTx: BtcTx, txid: TxId) test")
    fun constructTxTest1() {
        val btcTxDetail: BtcTxDetail = Mockito.mock(BtcTxDetail::class.java)
        Mockito.`when`(btcTxDetail.vout).thenReturn(1)
        Mockito.`when`(btcTxDetail.address).thenReturn("testaddress")
        Mockito.`when`(btcTxDetail.amount).thenReturn(BigDecimal.TEN)
        Mockito.`when`(btcTxDetail.fee).thenReturn(BigDecimal(-5))
        val btcTx: BtcTx = Mockito.mock(BtcTx::class.java)
        Mockito.`when`(btcTx.details).thenReturn(listOf(btcTxDetail))
        Mockito.`when`(btcTx.hash).thenReturn("hash")
        Mockito.`when`(btcTx.confirmations).thenReturn(0)
        val txid: TxId = Mockito.mock(TxId::class.java)
        Mockito.`when`(txid.index).thenReturn(1)

        val result: Tx = btcCoin.constructTx(btcTx, txid)
        Assertions.assertEquals(result.currency(), btcCoin.currency)
        Assertions.assertEquals(result.amount(), btcTxDetail.amount)
        Assertions.assertEquals(result.destination(), btcTxDetail.address)
        Assertions.assertEquals(result.fee(), btcTxDetail.fee.abs())
        Assertions.assertEquals(result.status(), TxStatus.VALIDATING)
        Assertions.assertEquals(result.hash(), btcTx.hash)
        Assertions.assertEquals(result.index(), btcTxDetail.vout)
    }

    @Test
    @DisplayName("constructTx(btcTxSinceBlock: BtcTxSinceBlock) test")
    fun constructTxTest2() {
        val btcTxSinceBlock = Mockito.mock(BtcTxSinceBlock::class.java)
        Mockito.`when`(btcTxSinceBlock.amount).thenReturn(BigDecimal.TEN)
        Mockito.`when`(btcTxSinceBlock.address).thenReturn("testaddress")
        Mockito.`when`(btcTxSinceBlock.confirmations).thenReturn(0)
        Mockito.`when`(btcTxSinceBlock.fee).thenReturn(BigDecimal(5))
        Mockito.`when`(btcTxSinceBlock.hash).thenReturn("hash")
        Mockito.`when`(btcTxSinceBlock.vout).thenReturn(1)

        val result = btcCoin.constructTx(btcTxSinceBlock)
        Assertions.assertEquals(result.currency(), btcCoin.currency)
        Assertions.assertEquals(result.amount(), btcTxSinceBlock.amount)
        Assertions.assertEquals(result.destination(), btcTxSinceBlock.address)
        Assertions.assertEquals(result.status(), TxStatus.VALIDATING)
        Assertions.assertEquals(result.fee(), btcTxSinceBlock.fee)
        Assertions.assertEquals(result.hash(), btcTxSinceBlock.hash)
        Assertions.assertEquals(result.index(), btcTxSinceBlock.vout)
    }
}