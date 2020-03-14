package dsx.bps.crypto.trx

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.TrxService
import dsx.bps.DBservices.TxService
import dsx.bps.config.currencies.TrxConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.common.Coin
import dsx.bps.crypto.trx.datamodel.TrxBlock
import dsx.bps.crypto.trx.datamodel.TrxTx
import dsx.bps.exception.crypto.trx.TrxException
import java.io.File
import java.math.BigDecimal
import kotlin.random.Random

class TrxCoin: Coin {

    override val currency = Currency.TRX
    override val config: Config

    private val account: String
    private val accountAddress: String
    private val privateKey: String
    private val trxService: TrxService
    private val txService: TxService

    override val connector: TrxRpc
    override val explorer: TrxExplorer

    private val confirmations: Int

    constructor(conf: Config, datasource: Datasource, txServ: TxService) {
        config = conf
        trxService = TrxService(datasource)
        txService = txServ

        account = config[TrxConfig.Coin.account]
        accountAddress = config[TrxConfig.Coin.accountAddress]
        privateKey = config[TrxConfig.Coin.privateKey]

        val host = config[TrxConfig.Connection.host]
        val port = config[TrxConfig.Connection.port]
        val url = "http://$host:$port/wallet/"
        connector = TrxRpc(url)

        confirmations = config[TrxConfig.Coin.confirmations]

        val frequency = config[TrxConfig.Explorer.frequency]
        explorer = TrxExplorer(this,datasource, txServ, frequency)
    }

    constructor(trxRpc: TrxRpc, trxExplorer: TrxExplorer, configPath: String, datasource: Datasource, txServ: TxService) {
        trxService = TrxService(datasource)
        txService = txServ
        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(TrxConfig)
            from.yaml.file(configFile)
        }
        config.validateRequired()

        account = config[TrxConfig.Coin.account]
        accountAddress = config[TrxConfig.Coin.accountAddress]
        privateKey = config[TrxConfig.Coin.privateKey]

        confirmations = config[TrxConfig.Coin.confirmations]

        connector = trxRpc
        explorer = trxExplorer
    }

    override fun getBalance(): BigDecimal = connector.getBalance(accountAddress)

    override fun getAddress(): String = accountAddress

    override fun getTag(): String? = Random.nextInt(0, Int.MAX_VALUE).toString()

    override fun getTx(txid: TxId): Tx {
        val trxTx = connector.getTransactionById(txid.hash)
        return constructTx(trxTx)
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
        val tx = connector.createTransaction(address, accountAddress, amount)
            .let {
                it.rawData.data = tag
                connector.getTransactionSign(privateKey, it)
            }

        val result = connector.broadcastTransaction(tx)

        if (result.success) {
            val transaction = constructTx(tx)
            val new = txService.add(
                transaction.status(), transaction.destination(), transaction.paymentReference(),
                transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency()
            )
            trxService.add(this.getAddress(),tx.ret.map { trxTxRet -> trxTxRet.contractRet }, new)
            return transaction
        } else {
            throw TrxException("Unable to broadcast TRX transaction $tx with response $result")
        }
    }

    fun getNowBlock(): TrxBlock = connector.getNowBlock()

    fun getBlockByNum(num: Int): TrxBlock = connector.getBlockByNum(num)

    fun getBlockById(hash: String): TrxBlock = connector.getBlockById(hash)

    fun constructTx(trxTx: TrxTx): Tx {
        val contract = trxTx.rawData.contract.first()
        val value = contract.parameter.value

        val txInfo = connector.getTransactionInfoById(trxTx.hash)
        val lastBlock = connector.getNowBlock()

        return object : Tx {
            override fun currency() = Currency.TRX

            override fun hash() = trxTx.hash

            override fun amount() = value.amount

            override fun destination() = value.toAddress

            override fun fee() = BigDecimal.ZERO

            override fun status(): TxStatus {
                val conf = lastBlock.blockHeader.rawData.number - txInfo.blockNumber
                return when {
                    conf < confirmations -> TxStatus.VALIDATING
                    else                 -> TxStatus.CONFIRMED
                }
            }

            override fun paymentReference(): String? = trxTx.rawData.data
        }
    }
}