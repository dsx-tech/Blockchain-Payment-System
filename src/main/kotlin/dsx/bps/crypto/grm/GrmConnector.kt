package dsx.bps.crypto.grm

import drinkless.org.ton.Client
import drinkless.org.ton.TonApi
import dsx.bps.connector.Connector
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.grm.datamodel.*
import dsx.bps.exception.connector.grm.GrmConnectorException
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GrmConnector : Connector {

    private val tonClient: Client

    constructor(tonClientConfig: String, keyStorePath: String, logVerbosityLevel: Int) {
        System.loadLibrary("native-lib")

        Client.execute(TonApi.SetLogVerbosityLevel(logVerbosityLevel))
        tonClient = Client.create(
            { result -> println(result) },
            { exception -> println(exception) },
            { exception -> println(exception) })


        tonClient.send(
            TonApi.Init(
                TonApi.Options(
                    TonApi.Config(
                        tonClientConfig,
                        "",
                        false,
                        false
                    ),
                    TonApi.KeyStoreTypeDirectory(keyStorePath)
                )
            ), null, null
        )
    }

    private suspend fun send(query: TonApi.Function): TonApi.Object {
        return suspendCoroutine<TonApi.Object> { cont ->
            tonClient.send(query, {
                cont.resume(it)
            }, null)
        }
    }

    fun getBalance(accountAddress: String): BigDecimal {
        val result = runBlocking {
            send(
                TonApi.GetAccountState(
                    TonApi.AccountAddress(accountAddress)
                )
            )
        }
        when (result) {
            is TonApi.FullAccountState -> return BigDecimal(result.balance)
            is TonApi.Error -> throw GrmConnectorException(
                "Error code: ${result.code}. Message: ${result.message}"
            )
            else -> throw GrmConnectorException(
                "${result.javaClass} cannot cast to TonApi.FullAccountState or TonApi.Error"
            )
        }
    }

    fun getTransaction(accountAddress: String, txId: TxId): GrmRawTransaction {
        val result = runBlocking {
            send(
                TonApi.RawGetTransactions(
                    null,
                    TonApi.AccountAddress(accountAddress),
                    TonApi.InternalTransactionId(
                        txId.index,
                        hexToByteArray(txId.hash)
                    )
                )
            )
        }
        when (result) {
            is TonApi.RawTransactions -> if (result.transactions.isEmpty()) {
                throw GrmConnectorException(
                    "Transaction with hash = ${txId.hash} and lt = ${txId.index}" +
                            " in account on address = $accountAddress not found."
                )
            } else {
                return GrmRawTransaction(result.transactions[0])
            }
            is TonApi.Error -> throw GrmConnectorException(
                "Error code: ${result.code}. Message: ${result.message}"
            )
            else -> throw GrmConnectorException(
                "${result.javaClass} cannot cast to TonApi.RawTransactions or TonApi.Error"
            )
        }
    }

    fun sendPaymentQuery(
        accountAddress: String, privateKey: String,
        localPassword: String, amount: BigDecimal,
        address: String, tag: String?, timeLimit: Int
    ): GrmQueryInfo {
        val inputKeyRegular: TonApi.InputKeyRegular = importKey(privateKey, localPassword)

        val msgData: TonApi.MsgData = TonApi.MsgDataText(tag?.toByteArray() ?: "".toByteArray())
        val msg: TonApi.MsgMessage = TonApi.MsgMessage(
            TonApi.AccountAddress(address), inputKeyRegular.key.publicKey, amount.toLong(), msgData
        )
        val actionMsg: TonApi.ActionMsg = TonApi.ActionMsg(arrayOf(msg), true)

        val query: TonApi.CreateQuery = TonApi.CreateQuery(
            inputKeyRegular,
            TonApi.AccountAddress(accountAddress), timeLimit, actionMsg
        )
        val resultCreateQuery: TonApi.Object = runBlocking {
            send(query)
        }

        val queryInfo: TonApi.QueryInfo = when (resultCreateQuery) {
            is TonApi.QueryInfo -> resultCreateQuery
            is TonApi.Error -> throw GrmConnectorException(
                "Error code: ${resultCreateQuery.code}. Message: ${resultCreateQuery.message}"
            )
            else -> throw GrmConnectorException(
                "${resultCreateQuery.javaClass} cannot cast to TonApi.QueryInfo or TonApi.Error"
            )
        }

        val resultSendQuery: TonApi.Object = runBlocking {
            send(TonApi.QuerySend(queryInfo.id))
        }

        when (resultSendQuery) {
            is TonApi.Ok -> return GrmQueryInfo(queryInfo)
            is TonApi.Error -> throw GrmConnectorException(
                "Error code: ${resultSendQuery.code}. Message: ${resultSendQuery.message}"
            )
            else -> throw GrmConnectorException(
                "${resultSendQuery.javaClass} cannot cast to TonApi.Ok or TonApi.Error"
            )
        }
    }

    private fun importKey(privateKey: String, localPassword: String): TonApi.InputKeyRegular {
        val result = runBlocking {
            send(
                TonApi.ImportUnencryptedKey(
                    localPassword.toByteArray(),
                    TonApi.ExportedUnencryptedKey(hexToByteArray(privateKey))
                )
            )
        }

        when (result) {
            is TonApi.Key -> return TonApi.InputKeyRegular(result, localPassword.toByteArray())
            is TonApi.Error -> throw GrmConnectorException(
                    "Error code: ${result.code}. Message: ${result.message}"
            )
            else -> throw GrmConnectorException(
                    "${result.javaClass} cannot cast to TonApi.Key or TonApi.Error"
            )
        }
    }

    fun getQueryEstimateFees(id: Long, ignoreCheckSig: Boolean): GrmQueryFees {
        val result = runBlocking {
            send(TonApi.QueryEstimateFees(id, ignoreCheckSig))
        }

        when (result) {
            is TonApi.QueryFees -> return GrmQueryFees(result)
            is TonApi.Error -> throw GrmConnectorException(
                    "Error code: ${result.code}. Message: ${result.message}"
            )
            else -> throw GrmConnectorException(
                    "${result.javaClass} cannot cast to TonApi.QueryFees or TonApi.Error"
            )
        }
    }

    fun getFullAccountState(accountAddress: String): GrmFullAccountState {
        val result = runBlocking {
            send(
                    TonApi.GetAccountState(
                            TonApi.AccountAddress(accountAddress)
                    )
            )
        }
        when (result) {
            is TonApi.FullAccountState -> return GrmFullAccountState(result)
            is TonApi.Error -> throw GrmConnectorException(
                "Error code: ${result.code}. Message: ${result.message}"
            )
            else -> throw GrmConnectorException(
                "${result.javaClass} cannot cast to TonApi.FullAccountState or TonApi.Error"
            )
        }
    }

    fun getLastInternalTxId(accountAddress: String): GrmInternalTxId {
        val result = runBlocking {
            send(
                TonApi.GetAccountState(
                    TonApi.AccountAddress(accountAddress)
                )
            )
        }
        when (result) {
            is TonApi.FullAccountState -> return GrmInternalTxId(result.lastTransactionId)
            is TonApi.Error -> throw GrmConnectorException(
                "Error code: ${result.code}. Message: ${result.message}"
            )
            else -> throw GrmConnectorException(
                "${result.javaClass} cannot cast to TonApi.FullAccountState or TonApi.Error"
            )
        }
    }

    /*
    Return max 10 Txs older than the sinceLastTxId including the sinceLastTxId.
    */
    fun getOlderAccountTxs(
        accountAddress: String,
        sinceLastTxId: GrmInternalTxId
    ): GrmRawTransactions {

        val result = runBlocking {
            send(
                TonApi.RawGetTransactions(
                    null,
                    TonApi.AccountAddress(accountAddress),
                    TonApi.InternalTransactionId(
                        sinceLastTxId.lt,
                        hexToByteArray(sinceLastTxId.hash)
                    )
                )
            )
        }
        when (result) {
            is TonApi.RawTransactions -> return GrmRawTransactions(result)
            is TonApi.Error -> throw GrmConnectorException(
                "Error code: ${result.code}. Message: ${result.message}"
            )
            else -> throw GrmConnectorException(
                "${result.javaClass} cannot cast to TonApi.RawTransactions or TonApi.Error"
            )
        }
    }
}