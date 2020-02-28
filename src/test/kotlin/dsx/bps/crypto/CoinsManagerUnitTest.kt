package dsx.bps.crypto

import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.btc.BtcCoin
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.trx.TrxCoin
import dsx.bps.crypto.xrp.XrpCoin
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.only
import java.math.BigDecimal

internal class CoinsManagerUnitTest {

    private val btcCoin: BtcCoin = Mockito.mock(BtcCoin::class.java)
    private val trxCoin: TrxCoin = Mockito.mock(TrxCoin::class.java)
    private val xrpCoin: XrpCoin = Mockito.mock(XrpCoin::class.java)
    private val coinsManager: CoinsManager

    init {
        val coinsMap: Map<Currency, Coin> = mapOf(
            Pair(Currency.BTC, btcCoin),
            Pair(Currency.XRP, xrpCoin),
            Pair(Currency.TRX, trxCoin)
        )
        coinsManager = CoinsManager(coinsMap)
    }

    @Test
    @DisplayName("getAddress pick right Coin")
    fun getAddressTest() {
        coinsManager.getAddress(Currency.BTC)
        Mockito.verify(btcCoin, only()).getAddress()
    }

    @Test
    @DisplayName("getBalance pick right Coin")
    fun getBalanceTest() {
        coinsManager.getBalance(Currency.BTC)
        Mockito.verify(btcCoin, only()).getBalance()
    }

    @Test
    @DisplayName("sendPayment pick right Coin")
    fun sendPaymentTest() {
        coinsManager.sendPayment(Currency.BTC, BigDecimal.ONE, "testaddress", 1)
        Mockito.verify(btcCoin, only()).sendPayment(BigDecimal.ONE, "testaddress", 1)
    }

    @Test
    @DisplayName("getTag pick right Coin")
    fun getTagTest() {
        coinsManager.getTag(Currency.BTC)
        Mockito.verify(btcCoin, only()).getTag()
    }

    @Test
    @DisplayName("getTx pick right Coin")
    fun getTxTest() {
        val txid = Mockito.mock(TxId::class.java)
        coinsManager.getTx(Currency.BTC, txid)
        Mockito.verify(btcCoin, only()).getTx(txid)
    }

    @Test
    @DisplayName("getTxs pick right Coin")
    fun getTxsTest() {
        val txid = Mockito.mock(TxId::class.java)
        val txids = listOf(txid)
        coinsManager.getTxs(Currency.BTC, txids)
        Mockito.verify(btcCoin).getTx(txid)
    }
}