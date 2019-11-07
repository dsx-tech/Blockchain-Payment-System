package dsx.bps.crypto.trx

import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.trx.datamodel.*
import dsx.bps.crypto.xrp.datamodel.TrxTxInfo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.lang.RuntimeException
import java.math.BigDecimal

internal class TrxClientUnitTest {

    private val trxRpc = Mockito.mock(TrxRpc::class.java)
    private val trxBlockchainListener = Mockito.mock(TrxBlockchainListener::class.java)
    private val trxClient = TrxClient(trxRpc, trxBlockchainListener)

    @Test
    @DisplayName("getBalance test")
    fun getBalanceTest(){
        trxClient.getBalance()
        //default value: accountAddress
        Mockito.verify(trxRpc, Mockito.only()).getBalance("41dc6c2bc46639e5371fe99e002858754604cf5847")
    }

    @Test
    @DisplayName("getAddress test")
    fun getAddressTest(){
        //default value: accountAddress
        Assertions.assertEquals(trxClient.getAddress(), "41dc6c2bc46639e5371fe99e002858754604cf5847")
    }

    @Test
    @DisplayName("getTag test")
    fun getTagTest(){
        val randomInt = trxClient.getTag()
        Assertions.assertTrue(randomInt is Int)
    }

    @Test
    @DisplayName("getTx test")
    fun getTxTest(){
        val txid = Mockito.mock(TxId::class.java)
        Mockito.`when`(txid.hash).thenReturn("hash")

        val trxTx = mockForConstructTx()

        Mockito.`when`(trxRpc.getTransactionById("hash")).thenReturn(trxTx)
        trxClient.getTx(txid)
    }

    @Nested
    inner class SendPaymentTest {
        @Test
        @DisplayName("sendPayment test: success broadcast result")
        fun sendPaymentTest1(){
            val trxTx = mockForConstructTx()

            //default private field values: addressAccount, privateKey
            Mockito.`when`(trxRpc.createTransaction("testaddress", "41dc6c2bc46639e5371fe99e002858754604cf5847", BigDecimal.TEN))
                .thenReturn(trxTx)
            Mockito.`when`(trxRpc.
                getTransactionSign("92f451c194301b4a1eae3a818cc181e1a319656dcdf6760cdbe35c54b05bb3ec", trxTx))
                .thenReturn(trxTx)

            val trxBroadcastTxResult = Mockito.mock(TrxBroadcastTxResult::class.java)
            Mockito.`when`(trxBroadcastTxResult.success).thenReturn(true)
            Mockito.`when`(trxRpc.broadcastTransaction(trxTx)).thenReturn(trxBroadcastTxResult)

            trxClient.sendPayment(BigDecimal.TEN,"testaddress",1)
        }

        @Test
        @DisplayName("sendPayment test: failure broadcast result")
        fun sendPaymentTest2(){
            val trxTxRawData = Mockito.mock(TrxTxRawData::class.java)
            val trxTx = Mockito.mock(TrxTx::class.java)
            Mockito.`when`(trxTx.rawData).thenReturn(trxTxRawData)

            //default private field values: addressAccount, privateKey
            Mockito.`when`(trxRpc.createTransaction("testaddress", "41dc6c2bc46639e5371fe99e002858754604cf5847", BigDecimal.TEN))
                .thenReturn(trxTx)
            Mockito.`when`(trxRpc.
                getTransactionSign("92f451c194301b4a1eae3a818cc181e1a319656dcdf6760cdbe35c54b05bb3ec", trxTx))
                .thenReturn(trxTx)

            val trxBroadcastTxResult = Mockito.mock(TrxBroadcastTxResult::class.java)
            Mockito.`when`(trxBroadcastTxResult.success).thenReturn(false)
            Mockito.`when`(trxRpc.broadcastTransaction(trxTx)).thenReturn(trxBroadcastTxResult)

            Assertions.assertThrows(RuntimeException::class.java){
                trxClient.sendPayment(BigDecimal.TEN,"testaddress",1)
            }
        }
    }

    @Test
    @DisplayName("getNowBlock test")
    fun getNowBlockTest(){
        trxClient.getNowBlock()
        Mockito.verify(trxRpc, Mockito.only()).getNowBlock()
    }

    @Test
    @DisplayName("getBlockByNum test")
    fun getBlockByNumTest(){
        trxClient.getBlockByNum(1)
        Mockito.verify(trxRpc, Mockito.only()).getBlockByNum(1)
    }

    @Test
    @DisplayName("getBlockById test")
    fun getBlockById(){
        trxClient.getBlockById("hash")
        Mockito.verify(trxRpc, Mockito.only()).getBlockById("hash")
    }

    @Test
    @DisplayName("constructTx test")
    fun constructTxTest(){
        val trxTxInfo = Mockito.mock(TrxTxInfo::class.java)
        Mockito.`when`(trxTxInfo.blockNumber).thenReturn(1)
        Mockito.`when`(trxRpc.getTransactionInfoById("hash")).thenReturn(trxTxInfo)

        val lastTrxBlockRawData = Mockito.mock(TrxBlockRawData::class.java)
        Mockito.`when`(lastTrxBlockRawData.number).thenReturn(1)
        val lastTrxBlockHeader = Mockito.mock(TrxBlockHeader::class.java)
        Mockito.`when`(lastTrxBlockHeader.rawData).thenReturn(lastTrxBlockRawData)
        val lastTrxBlock = Mockito.mock(TrxBlock::class.java)
        Mockito.`when`(lastTrxBlock.blockHeader).thenReturn(lastTrxBlockHeader)
        Mockito.`when`(trxRpc.getNowBlock()).thenReturn(lastTrxBlock)

        val trxTxValue = Mockito.mock(TrxTxValue::class.java)
        Mockito.`when`(trxTxValue.amount).thenReturn(BigDecimal.TEN)
        Mockito.`when`(trxTxValue.toAddress).thenReturn("testaddress")
        val trxTxParameter = Mockito.mock(TrxTxParameter::class.java)
        Mockito.`when`(trxTxParameter.value).thenReturn(trxTxValue)
        val trxTxContract = Mockito.mock(TrxTxContract::class.java)
        Mockito.`when`(trxTxContract.parameter).thenReturn(trxTxParameter)
        val trxTxRawData = Mockito.mock(TrxTxRawData::class.java)
        Mockito.`when`(trxTxRawData.contract).thenReturn(listOf(trxTxContract))
        Mockito.`when`(trxTxRawData.data).thenReturn("1")
        val trxTx = Mockito.mock(TrxTx::class.java)
        Mockito.`when`(trxTx.rawData).thenReturn(trxTxRawData)
        Mockito.`when`(trxTx.hash).thenReturn("hash")

        val resultTx = trxClient.constructTx(trxTx)
        Assertions.assertEquals(resultTx.currency(), trxClient.currency)
        Assertions.assertEquals(resultTx.hash(),"hash")
        Assertions.assertEquals(resultTx.amount(), trxTxValue.amount)
        Assertions.assertEquals(resultTx.destination(), trxTxValue.toAddress)
        Assertions.assertEquals(resultTx.fee(), BigDecimal.ZERO)
        Assertions.assertEquals(resultTx.status(), TxStatus.VALIDATING)
        Assertions.assertEquals(resultTx.tag(),trxTxRawData.data?.toInt(16))
    }

    private fun mockForConstructTx(): TrxTx{
        val trxTxInfo = Mockito.mock(TrxTxInfo::class.java)
        Mockito.`when`(trxTxInfo.blockNumber).thenReturn(1)
        Mockito.`when`(trxRpc.getTransactionInfoById("hash")).thenReturn(trxTxInfo)

        val lastTrxBlockRawData = Mockito.mock(TrxBlockRawData::class.java)
        Mockito.`when`(lastTrxBlockRawData.number).thenReturn(1)
        val lastTrxBlockHeader = Mockito.mock(TrxBlockHeader::class.java)
        Mockito.`when`(lastTrxBlockHeader.rawData).thenReturn(lastTrxBlockRawData)
        val lastTrxBlock = Mockito.mock(TrxBlock::class.java)
        Mockito.`when`(lastTrxBlock.blockHeader).thenReturn(lastTrxBlockHeader)
        Mockito.`when`(trxRpc.getNowBlock()).thenReturn(lastTrxBlock)

        val trxTxValue = Mockito.mock(TrxTxValue::class.java)
        Mockito.`when`(trxTxValue.amount).thenReturn(BigDecimal.TEN)
        Mockito.`when`(trxTxValue.toAddress).thenReturn("testaddress")
        val trxTxParameter = Mockito.mock(TrxTxParameter::class.java)
        Mockito.`when`(trxTxParameter.value).thenReturn(trxTxValue)
        val trxTxContract = Mockito.mock(TrxTxContract::class.java)
        Mockito.`when`(trxTxContract.parameter).thenReturn(trxTxParameter)
        val trxTxRawData = Mockito.mock(TrxTxRawData::class.java)
        Mockito.`when`(trxTxRawData.contract).thenReturn(listOf(trxTxContract))
        Mockito.`when`(trxTxRawData.data).thenReturn("1")
        val trxTx = Mockito.mock(TrxTx::class.java)
        Mockito.`when`(trxTx.rawData).thenReturn(trxTxRawData)
        Mockito.`when`(trxTx.hash).thenReturn("hash")

        return trxTx
    }
}