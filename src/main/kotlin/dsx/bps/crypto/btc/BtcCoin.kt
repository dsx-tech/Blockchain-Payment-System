package dsx.bps.crypto.btc

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.currencies.BtcConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.btc.datamodel.*
import dsx.bps.crypto.common.Coin
import java.io.File
import java.math.BigDecimal

class BtcCoin: Coin {

    override val currency = Currency.BTC
    override val config: Config

    override val connection: BtcRpc
    override val explorer: BtcExplorer

    private val confirmations: Int

    constructor(conf: Config) {
        config = conf

        val user = config[BtcConfig.Coin.user]
        val pass = config[BtcConfig.Coin.password]
        val host = config[BtcConfig.Connection.host]
        val port = config[BtcConfig.Connection.port]
        val url = "http://$user:$pass@$host:$port/"
        connection = BtcRpc(url)

        confirmations = config[BtcConfig.Coin.confirmations]

        val frequency = config[BtcConfig.Explorer.frequency]
        explorer = BtcExplorer(this, frequency)
    }

    constructor(btcRpc: BtcRpc, btcExplorer: BtcExplorer, configPath: String) {
        val configFile = File(configPath)
        config = with(Config()) {
            addSpec(BtcConfig)
            from.yaml.file(configFile)
        }
        config.validateRequired()

        confirmations = config[BtcConfig.Coin.confirmations]
        connection = btcRpc
        explorer = btcExplorer
    }

    override fun getBalance(): BigDecimal = connection.getBalance()

    override fun getAddress(): String = connection.getNewAddress()

    override fun getTag(): Int? = null

    override fun getTx(txid: TxId): Tx {
        val btcTx = connection.getTransaction(txid.hash)
        return constructTx(btcTx, txid)
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: Int?): Tx {
        val tx = connection.createRawTransaction(amount, address)
            .let { connection.fundRawTransaction(it) }
            // TODO: implement local tx sign
            .let { connection.signRawTransactionWithWallet(it) }
            .let { connection.sendRawTransaction(it) }
            .let { connection.getTransaction(it) }

        val detail = tx
            .details
            .single { detail -> match(detail, amount, address) }

        return constructTx(tx, TxId(tx.hash, detail.vout))
    }

    private fun match(detail: BtcTxDetail, amount: BigDecimal, address: String): Boolean =
        detail.address == address &&
        detail.category == "send" &&
        detail.amount.abs().compareTo(amount.abs()) == 0

    fun getBestBlockHash(): String = connection.getBestBlockHash()

    fun getBlock(hash: String): BtcBlock = connection.getBlock(hash)

    fun listSinceBlock(hash: String): BtcListSinceBlock = connection.listSinceBlock(hash)

    fun constructTx(btcTx: BtcTx, txid: TxId): Tx {
        val detail = btcTx
            .details
            .first { detail -> detail.vout == txid.index }

        return object: Tx {
            override fun currency() = Currency.BTC

            override fun hash() = btcTx.hash

            override fun index() = detail.vout

            override fun amount() = detail.amount.abs()

            override fun destination() = detail.address

            override fun fee() = detail.fee.abs()

            override fun status() = when {
                btcTx.confirmations < 0             -> TxStatus.REJECTED
                btcTx.confirmations < confirmations -> TxStatus.VALIDATING
                else                                -> TxStatus.CONFIRMED
            }
        }
    }

    fun constructTx(btcTxSinceBlock: BtcTxSinceBlock): Tx = object: Tx {

        override fun currency() = Currency.BTC

        override fun hash() = btcTxSinceBlock.hash

        override fun index() = btcTxSinceBlock.vout

        override fun amount() = btcTxSinceBlock.amount

        override fun destination() = btcTxSinceBlock.address

        override fun fee() = btcTxSinceBlock.fee

        override fun status() = when {
            btcTxSinceBlock.confirmations < 0             -> TxStatus.REJECTED
            btcTxSinceBlock.confirmations < confirmations -> TxStatus.VALIDATING
            else                                          -> TxStatus.CONFIRMED
        }
    }
}