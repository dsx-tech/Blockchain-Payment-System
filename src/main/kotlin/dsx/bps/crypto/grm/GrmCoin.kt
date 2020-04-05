package dsx.bps.crypto.grm

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.GrmService
import dsx.bps.DBservices.TxService
import dsx.bps.config.currencies.GrmConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.grm.datamodel.GrmFullAccountState
import dsx.bps.crypto.grm.datamodel.GrmInternalTxId
import dsx.bps.crypto.grm.datamodel.GrmRawTransaction
import dsx.bps.exception.crypto.grm.payment.GrmPaymentFailProcessTxException
import dsx.bps.exception.crypto.grm.payment.GrmPaymentTimeLimitException
import java.io.File
import java.math.BigDecimal
import kotlin.random.Random

class GrmCoin : Coin {

    override val currency: Currency = Currency.GRM
    override val config: Config

    private val accountAddress: String
    private val privateKey: String
    private val localPassword: String
    private val paymentQueryTimeLimit: Int
    private val numberAttemptsSendPaymentQuery: Int
    private val grmService: GrmService
    private val txService: TxService

    override val connector: GrmConnector
    override val explorer: GrmExplorer

    constructor(conf: Config, datasource: Datasource, txServ: TxService) {
        config = conf
        grmService = GrmService(datasource)
        txService = txServ

        accountAddress = config[GrmConfig.Coin.accountAddress]
        privateKey = config[GrmConfig.Coin.privateKey]
        localPassword = config[GrmConfig.Coin.localPassword]
        paymentQueryTimeLimit = config[GrmConfig.Coin.paymentQueryTimeLimit]
        numberAttemptsSendPaymentQuery = config[GrmConfig.Coin.numberAttemptsSendPaymentQuery]

        val tonClientConfig = File(config[GrmConfig.Connection.pathToTonClientConfig]).readText()
        val keyStorePath = config[GrmConfig.Connection.keyStorePath]
        val logVerbosityLevel = config[GrmConfig.Connection.logVerbosityLevel]
        connector = GrmConnector(tonClientConfig, keyStorePath, logVerbosityLevel)

        val frequency = config[GrmConfig.Explorer.frequency]
        explorer = GrmExplorer(this, datasource, txServ, frequency)
    }

    constructor(
        grmConnection: GrmConnector, grmExplorer: GrmExplorer,
        configPath: String, datasource: Datasource, txServ: TxService
    ) {
        grmService = GrmService(datasource)
        txService = txServ
        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(GrmConfig)
            from.yaml.file(configFile)
        }

        config.validateRequired()

        accountAddress = config[GrmConfig.Coin.accountAddress]
        privateKey = config[GrmConfig.Coin.privateKey]
        localPassword = config[GrmConfig.Coin.localPassword]
        paymentQueryTimeLimit = config[GrmConfig.Coin.paymentQueryTimeLimit]
        numberAttemptsSendPaymentQuery = config[GrmConfig.Coin.numberAttemptsSendPaymentQuery]

        connector = grmConnection
        explorer = grmExplorer
    }


    override fun getBalance(): BigDecimal = connector.getBalance(accountAddress)

    override fun getAddress(): String = accountAddress

    override fun getTag(): String? = Random.nextInt(0, Int.MAX_VALUE).toString()

    override fun getTx(txid: TxId): Tx {
        val grmRawTransaction = connector.getTransaction(accountAddress, txid)
        return constructDepositTx(grmRawTransaction)
    }

    override fun updateTxStatus(txid: TxId): Tx {

        val ourTx = connector.getTransaction(accountAddress, txid)
        val otherAccountAddress = ourTx.outMsg.single().destination
        val otherAccountLastTx = connector.getLastInternalTxId(otherAccountAddress)
        val otherAccountTxs = getAccountTxsUntilLt(
            otherAccountAddress,
            otherAccountLastTx, ourTx.outMsg.single().createdLt
        )

        for (tx in otherAccountTxs) {
            if (tx.inMsg.bodyHash == ourTx.outMsg.single().bodyHash) {
                return if (tx.outMsg.isEmpty()) {
                    constructPaymentTx(ourTx, TxStatus.CONFIRMED)
                } else {
                    if (tx.outMsg.size == 1) {
                        if (tx.outMsg.single().destination == accountAddress &&
                            tx.outMsg.single().value == ourTx.outMsg.single().value
                        ) {
                            constructPaymentTx(ourTx, TxStatus.REJECTED)
                        } else {
                            constructPaymentTx(ourTx, TxStatus.CONFIRMED)
                        }
                    } else {
                        constructPaymentTx(ourTx, TxStatus.CONFIRMED)
                    }
                }
            }
        }

        return constructPaymentTx(ourTx, TxStatus.VALIDATING)
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
        //TODO: Need tests
        var curAttemptSendPaymentQuery = 0
        while (curAttemptSendPaymentQuery < numberAttemptsSendPaymentQuery) {
            var lastTxId = connector.getLastInternalTxId(accountAddress)

            val queryInfo = connector.sendPaymentQuery(
                accountAddress, privateKey,
                localPassword, amount, address, tag, paymentQueryTimeLimit
            )

            var paymentTxChecking = true

            while (paymentTxChecking) {
                val accountState = connector.getFullAccountState(accountAddress)
                val newTxId = accountState.lastTxId

                val notProcessTxs = getAccountTxs(newTxId, lastTxId)
                for (tx in notProcessTxs.reversedArray()) {
                    if (tx.utime > queryInfo.validUntil) {
                        paymentTxChecking = false
                        break
                    }

                    if (tx.inMsg.bodyHash == queryInfo.bodyHash &&
                        tx.inMsg.source == "" &&
                        tx.inMsg.msgText == tag ?: ""
                    ) {
                        if (tx.outMsg.size == 1) {
                            if (tx.outMsg.single().destination == address &&
                                tx.outMsg.single().value == amount.toLong()
                            ) {
                                return constructPaymentTx(tx, TxStatus.VALIDATING)
                            } else
                                throw GrmPaymentFailProcessTxException(
                                    "Payment's tx $tx send message to wrong destination" +
                                            " address or with wrong amount."
                                )
                        } else if (tx.outMsg.isEmpty()) {
                            throw GrmPaymentFailProcessTxException(
                                "Payment query failed: $tx have no out message"
                            )
                        } else
                            throw GrmPaymentFailProcessTxException(
                                "Payment's tx $tx has more than one out message."
                            )
                    }
                }
                lastTxId = newTxId
                if (accountState.syncUtime > queryInfo.validUntil) {
                    paymentTxChecking = false
                }
            }
            curAttemptSendPaymentQuery += 1
        }

        throw GrmPaymentTimeLimitException(
            "Payment query not processed in blockchain in $numberAttemptsSendPaymentQuery attempts"
        )
    }

    fun getFullAccountState(): GrmFullAccountState {
        return connector.getFullAccountState(accountAddress)
    }

    fun getLastInternalTxId(): GrmInternalTxId {
        return connector.getLastInternalTxId(accountAddress)
    }

    /*
    Include startTx and not include untilTx
     */
    fun getAccountTxs(startTxId: GrmInternalTxId, untilTxId: GrmInternalTxId): Array<GrmRawTransaction> {
        val grmTxs = ArrayList<GrmRawTransaction>()

        var tmpTxId = startTxId
        var allNewTxsProcessed = false

        while (!allNewTxsProcessed) {
            val olderAccountTxs = connector.getOlderAccountTxs(
                accountAddress, tmpTxId
            )
            for (grmOlderTx in olderAccountTxs.transactions) {
                if (grmOlderTx.transactionId == untilTxId) {
                    allNewTxsProcessed = true
                    break
                }
                grmTxs.add(grmOlderTx)
            }
            if (olderAccountTxs.previousTransactionId.lt == 0L &&
                olderAccountTxs.previousTransactionId.hash ==
                "0000000000000000000000000000000000000000000000000000000000000000"
            ) {
                allNewTxsProcessed = true
            }
            tmpTxId = olderAccountTxs.previousTransactionId
        }
        return grmTxs.toTypedArray()
    }

    fun getAccountTxsUntilLt(
        accountAddress: String, startTxId: GrmInternalTxId,
        untilLt: Long
    ): Array<GrmRawTransaction> {
        val grmTxs = ArrayList<GrmRawTransaction>()

        var tmpTxId = startTxId
        var allNewTxsProcessed = false

        while (!allNewTxsProcessed) {
            val olderAccountTxs = connector.getOlderAccountTxs(
                accountAddress, tmpTxId
            )
            for (grmOlderTx in olderAccountTxs.transactions) {
                if (grmOlderTx.transactionId.lt < untilLt) {
                    allNewTxsProcessed = true
                    break
                }
                grmTxs.add(grmOlderTx)
            }
            if (olderAccountTxs.previousTransactionId.lt == 0L &&
                olderAccountTxs.previousTransactionId.hash ==
                "0000000000000000000000000000000000000000000000000000000000000000"
            ) {
                allNewTxsProcessed = true
            }
            tmpTxId = olderAccountTxs.previousTransactionId
        }
        return grmTxs.toTypedArray()
    }

    fun constructDepositTx(grmTx: GrmRawTransaction): Tx {

        return object : Tx {
            override fun currency() = currency
            override fun hash() = grmTx.transactionId.hash
            override fun txid(): TxId = TxId(hash(), grmTx.transactionId.lt)
            override fun amount(): BigDecimal {
                var amount = grmTx.inMsg.value
                for (msg in grmTx.outMsg) {
                    amount -= msg.value + msg.ihrFee + msg.fwdFee
                }
                return BigDecimal(amount)
            }

            override fun destination() = grmTx.inMsg.destination
            override fun paymentReference(): String? = grmTx.inMsg.msgText
            override fun fee() = BigDecimal(grmTx.fee)
            override fun status() = TxStatus.CONFIRMED
        }
    }

    fun constructPaymentTx(grmTx: GrmRawTransaction, statusTx: TxStatus): Tx {
        return object : Tx {
            override fun currency() = currency
            override fun txid() = TxId(grmTx.transactionId.hash, grmTx.transactionId.lt)
            override fun hash() = grmTx.transactionId.hash
            override fun amount(): BigDecimal {
                var amount = grmTx.inMsg.value
                for (msg in grmTx.outMsg) {
                    amount -= msg.value + msg.ihrFee + msg.fwdFee
                }
                return BigDecimal(amount)
            }

            override fun destination() = grmTx.outMsg.single().destination
            override fun paymentReference() = grmTx.inMsg.msgText
            override fun fee() = BigDecimal(grmTx.fee)
            override fun status() = statusTx
        }
    }

}