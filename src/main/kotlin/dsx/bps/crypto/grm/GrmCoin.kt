package dsx.bps.crypto.grm

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.DBservices.crypto.grm.GrmInMsgService
import dsx.bps.DBservices.crypto.grm.GrmOutMsgService
import dsx.bps.DBservices.crypto.grm.GrmQueryInfoService
import dsx.bps.DBservices.crypto.grm.GrmTxService
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

    // emptyTxLt and emptyTxHash are used to determine when there are no earlier smart contract transactions
    private val emptyTxLt: Long = 0
    private val emptyTxHash = "0000000000000000000000000000000000000000000000000000000000000000"

    // validatingTxIndex are used to determine payment Tx in getTx()
    private val validatingTxIndex: Long = -1

    private val grmInMsgService: GrmInMsgService
    private val grmOutMsgService: GrmOutMsgService
    private val grmQueryInfoService: GrmQueryInfoService
    private val grmTxService: GrmTxService
    private val txService: TxService

    override val connector: GrmConnector
    override val explorer: GrmExplorer

    constructor(conf: Config, datasource: Datasource, txServ: TxService) {
        config = conf
        grmInMsgService = GrmInMsgService(datasource)
        grmOutMsgService = GrmOutMsgService(datasource)
        grmQueryInfoService = GrmQueryInfoService(datasource)
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
        explorer = GrmExplorer(
                this, grmInMsgService, grmOutMsgService,
                grmTxService, txServ, frequency
        )
    }

    constructor(
            grmConnection: GrmConnector, grmExplorer: GrmExplorer,
            configPath: String, grmInMsgServ: GrmInMsgService,
            grmOutMsgServ: GrmOutMsgService, grmQueryInfoServ: GrmQueryInfoService,
            grmTxServ: GrmTxService, txServ: TxService
    ) {
        grmInMsgService = grmInMsgServ
        grmOutMsgService = grmOutMsgServ
        grmQueryInfoService = grmQueryInfoServ
        grmTxService = grmTxServ
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
        if (txid.index == validatingTxIndex) {
            return updatePaymentTx(txid)
        } else {
            val grmRawTransaction = connector.getTransaction(accountAddress, txid)
            return constructDepositTx(grmRawTransaction)
        }
    }

    private fun updatePaymentTx(txid: TxId): Tx {
        val txEntity = txService.getByTxId(txid.hash, validatingTxIndex)
        val grmTx = grmTxService.findByInMsgHashAndDest(txid.hash, txEntity.destination)

        if (grmTx == null) {
            return if (explorer.syncUtime <= grmQueryInfoService.findByHash(txid.hash)?.validUntil ?: -1) {
                txService.constructTxByTxEntity(txEntity)
            } else {
                object : Tx {
                    override fun currency() = txEntity.currency
                    override fun hash() = txEntity.hash
                    override fun amount(): BigDecimal = txEntity.amount
                    override fun destination() = txEntity.destination
                    override fun paymentReference(): String? = txEntity.tag
                    override fun fee() = txEntity.fee
                    override fun status() = TxStatus.REJECTED
                }
            }
        } else {
            return txService.constructTxByTxEntity(grmTx.tx)
        }
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
        val queryInfo = connector.sendPaymentQuery(
                accountAddress, privateKey,
                localPassword, amount, address, tag, paymentQueryTimeLimit
        )

        val queryEstimateFees = connector.getQueryEstimateFees(queryInfo.id, true)

        grmQueryInfoService.add(queryInfo.id, queryInfo.validUntil, queryInfo.bodyHash)

        val resultTx = object : Tx {
            override fun currency(): Currency = Currency.GRM
            override fun txid(): TxId = TxId(hash(), -1)
            override fun hash(): String = queryInfo.bodyHash
            override fun amount(): BigDecimal = amount
            override fun destination(): String = address
            override fun paymentReference(): String? = tag
            override fun fee(): BigDecimal = BigDecimal(
                    queryEstimateFees.sourceFees.fwdFee + queryEstimateFees.sourceFees.gasFee
            )

            override fun status(): TxStatus = TxStatus.VALIDATING
        }

        txService.add(
                resultTx.status(), resultTx.destination(),
                resultTx.paymentReference(), resultTx.amount(),
                resultTx.fee(), resultTx.txid().hash, resultTx.txid().index,
                resultTx.currency()
        )
        return resultTx
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
            override fun txid(): TxId = TxId(grmTx.transactionId.hash, grmTx.transactionId.lt)
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

    fun constructPaymentTx(
            hash: String, lt: Long, amount: BigDecimal, destination: String,
            paymentReference: String?, fee: BigDecimal): Tx {
        return object : Tx {
            override fun currency() = currency
            override fun hash() = hash
            override fun txid(): TxId = TxId(hash, lt)
            override fun amount(): BigDecimal = amount
            override fun destination() = destination
            override fun paymentReference() = paymentReference
            override fun fee() = fee
            override fun status() = TxStatus.CONFIRMED
        }
    }

}