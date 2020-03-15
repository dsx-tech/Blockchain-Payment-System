package dsx.bps.crypto.grm

import dsx.bps.connection.Connector
import dsx.bps.core.datamodel.TxId
import dsx.bps.crypto.grm.datamodel.*
import dsx.bps.exception.connector.grm.GrmConnectorException
import dsx.bps.ton.api.TonApi
import dsx.bps.ton.api.TonClient
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GrmConnector : Connector {

    private val tonClient: TonClient

    constructor(tonClientConfig: String, keyStorePath: String, logVerbosityLevel: Int) {
        System.loadLibrary("native-lib")

        TonClient.execute(TonApi.SetLogVerbosityLevel(logVerbosityLevel))
        tonClient = TonClient.create(
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