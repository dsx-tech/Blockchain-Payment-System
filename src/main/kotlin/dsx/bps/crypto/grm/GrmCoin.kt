package dsx.bps.crypto.grm

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.GrmTxService
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
import dsx.bps.crypto.grm.datamodel.byteArrayToHex
import dsx.bps.exception.crypto.grm.payment.GrmPaymentFailProcessTxException
import dsx.bps.exception.crypto.grm.payment.GrmPaymentTimeLimitException
import java.io.File
import java.math.BigDecimal
import kotlin.random.Random

class GrmCoin : Coin {

    override val currency: Currency = Currency.GRM
    override val config: Config

    val accountAddress: String
    private val privateKey: String
    private val localPassword: String
    private val paymentQueryTimeLimit: Int
    private val numberAttemptsSendPaymentQuery: Int
    private val lengthTagInBytes: Int
    val lengthTagInHex: Int
        get() = this.lengthTagInBytes * 2

    //emptyTxLt and emptyTxHash are used to determine when there are no earlier smart contract transactions
    private val emptyTxLt = 0L
    private val emptyTxHash = "0000000000000000000000000000000000000000000000000000000000000000"

    private val grmTxService: GrmTxService
    private val txService: TxService

    override val connector: GrmConnector
    override val explorer: GrmExplorer

    constructor(conf: Config, datasource: Datasource, txServ: TxService) {
        config = conf
        grmTxService = GrmTxService(datasource)
        txService = txServ

        accountAddress = config[GrmConfig.Coin.accountAddress]
        privateKey = config[GrmConfig.Coin.privateKey]
        localPassword = config[GrmConfig.Coin.localPassword]
        paymentQueryTimeLimit = config[GrmConfig.Coin.paymentQueryTimeLimit]
        numberAttemptsSendPaymentQuery = config[GrmConfig.Coin.numberAttemptsSendPaymentQuery]
        lengthTagInBytes = config[GrmConfig.Coin.lengthTagInBytes]

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
        grmTxService = GrmTxService(datasource)
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
        lengthTagInBytes = config[GrmConfig.Coin.lengthTagInBytes]

        connector = grmConnection
        explorer = grmExplorer
    }

    override fun getBalance(): BigDecimal = connector.getBalance(accountAddress)

    override fun getAddress(): String = accountAddress

    override fun getTag(): String? = byteArrayToHex(Random.nextBytes(lengthTagInBytes))

    // Returns only *accountAddress* transactions
    override fun getTx(txid: TxId): Tx {
        val grmRawTransaction = connector.getTransaction(accountAddress, txid)
        return constructDepositTx(grmRawTransaction)
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
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

                val notProcessTxs = getAccountTxs(accountAddress, newTxId, lastTxId)

                for (i in notProcessTxs.size - 1 downTo 0) {
                    val tx = notProcessTxs[i]
                    if (tx.utime > queryInfo.validUntil) {
                        paymentTxChecking = false
                        break
                    }

                    if (tx.inMsg.bodyHash == queryInfo.bodyHash &&
                        tx.inMsg.source == ""
                    ) {
                        if (tx.outMsg.size == 1) {
                            if (tx.outMsg.single().destination == address &&
                                tx.outMsg.single().value == amount.toLong() &&
                                tx.outMsg.single().msgText == tag ?: ""
                            ) {
                                return constructPaymentTx(tx)
                            } else
                                throw GrmPaymentFailProcessTxException(
                                    "Payment's tx $tx send message to wrong destination" +
                                            " address or with wrong amount or tag."
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
    fun getAccountTxs(
        accountAddress: String, startTxId: GrmInternalTxId,
        untilTxId: GrmInternalTxId
    ): Array<GrmRawTransaction> {
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
            if (olderAccountTxs.previousTransactionId.lt == emptyTxLt &&
                olderAccountTxs.previousTransactionId.hash == emptyTxHash
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

    fun constructPaymentTx(grmTx: GrmRawTransaction): Tx {
        return object : Tx {
            override fun currency() = currency
            override fun txid() = TxId(grmTx.transactionId.hash, grmTx.transactionId.lt)
            override fun hash() = grmTx.transactionId.hash
            override fun amount(): BigDecimal {
                var amount = grmTx.outMsg.single().value
                for (msg in grmTx.outMsg) {
                    amount -= msg.value + msg.ihrFee + msg.fwdFee
                }
                return BigDecimal(amount)
            }

            override fun destination() = grmTx.outMsg.single().destination
            override fun paymentReference() = grmTx.outMsg.single().msgText
            override fun fee() = BigDecimal(grmTx.fee)
            override fun status() = TxStatus.CONFIRMED
        }
    }

}