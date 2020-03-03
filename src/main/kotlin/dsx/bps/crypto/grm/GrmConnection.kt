package dsx.bps.crypto.grm

import dsx.bps.connection.Connection
import dsx.bps.crypto.grm.datamodel.GrmFullAccountState
import dsx.bps.crypto.grm.datamodel.GrmInternalTxId
import dsx.bps.crypto.grm.datamodel.GrmRawTransaction
import dsx.bps.crypto.grm.datamodel.GrmRawTransactions
import dsx.bps.ton.api.TonApi
import dsx.bps.ton.api.TonClient
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GrmConnection : Connection {

    private val tonClient: TonClient

    constructor(tonClientConfig: String, keyStorePath: String, logVerbosityLevel: Int) {
        System.loadLibrary("native-lib")

        TonClient.execute(TonApi.SetLogVerbosityLevel(logVerbosityLevel))
        tonClient = TonClient.create(null, null, null)


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
        var fullAccountState: TonApi.FullAccountState = TonApi.FullAccountState()

        runBlocking {
            fullAccountState = send(
                TonApi.GetAccountState(
                    TonApi.AccountAddress(accountAddress)
                )
            )
                    as TonApi.FullAccountState
        }
        return BigDecimal(fullAccountState.balance)
    }

    fun getFullAccountState(accountAddress: String): GrmFullAccountState {
        var fullAccountState: TonApi.FullAccountState = TonApi.FullAccountState()

        runBlocking {
            fullAccountState = send(
                TonApi.GetAccountState(
                    TonApi.AccountAddress(accountAddress)
                )
            )
                    as TonApi.FullAccountState
        }

        return GrmFullAccountState(fullAccountState)
    }

    fun getLastInternalTxId(accountAddress: String): GrmInternalTxId {
        var fullAccountState: TonApi.FullAccountState = TonApi.FullAccountState()

        runBlocking {
            fullAccountState = send(
                TonApi.GetAccountState(
                    TonApi.AccountAddress(accountAddress)
                )
            )
                    as TonApi.FullAccountState
        }

        return GrmInternalTxId(fullAccountState.lastTransactionId)
    }

    fun getAccountTxs(
        accountAddress: String,
        sinceLastTxId: GrmInternalTxId
    ): Array<GrmRawTransaction> {

        var rawTxs: TonApi.RawTransactions = TonApi.RawTransactions()
        runBlocking {
            rawTxs = send(
                TonApi.RawGetTransactions(
                    null,
                    TonApi.AccountAddress(accountAddress),
                    TonApi.InternalTransactionId(
                        sinceLastTxId.lt,
                        sinceLastTxId.hash
                    )
                )
            ) as TonApi.RawTransactions
        }

        return GrmRawTransactions(rawTxs).transactions
    }
}