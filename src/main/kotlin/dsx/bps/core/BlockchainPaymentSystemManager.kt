package dsx.bps.core

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.config.BPSConfig
import dsx.bps.config.DatabaseConfig
import dsx.bps.config.DepositAccountProcessorConfig
import dsx.bps.config.InvoiceProcessorConfig
import dsx.bps.config.PaymentProcessorConfig
import dsx.bps.core.datamodel.*
import dsx.bps.crypto.CoinsManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.math.BigDecimal
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BlockchainPaymentSystemManager {

    companion object {
        private val DEFAULT_CONFIG_PATH = this::class.java.getResource("/BpsConfig.yaml").path
    }

    private val coinsManager: CoinsManager
    private val emitter: Observable<Tx>
    private val datasource = Datasource()
    private val invoiceProcessor: InvoiceProcessor
    private val paymentProcessor: PaymentProcessor
    private val depositAccountProcessor: DepositAccountProcessor

    constructor(confPath: String = DEFAULT_CONFIG_PATH) {
        val configFile = File(confPath)

        val databaseConfig = with(Config()) {
            addSpec(DatabaseConfig)
            from.yaml.file(configFile)
        }
        databaseConfig.validateRequired()

        datasource.initConnection(databaseConfig)
        val txService = TxService()
        coinsManager = CoinsManager(configFile, txService)

        val bpsConfig = with(Config()) {
            addSpec(BPSConfig)
            from.yaml.file(configFile)
        }
        bpsConfig.validateRequired()
        val threadPool: ExecutorService = Executors.newFixedThreadPool(bpsConfig[BPSConfig.threadsForInvoiceObserver])
        emitter = coinsManager.getAllEmitters().observeOn(Schedulers.from(threadPool))

        val invoiceProcessorConfig = with(Config()) {
            addSpec(InvoiceProcessorConfig)
            from.yaml.file(configFile)
        }
        invoiceProcessorConfig.validateRequired()

        val paymentProcessorConfig = with(Config()) {
            addSpec(PaymentProcessorConfig)
            from.yaml.file(configFile)
        }
        paymentProcessorConfig.validateRequired()

        val depositAccountProcessorConfig = with(Config()) {
            addSpec(DepositAccountProcessorConfig)
            from.yaml.file(configFile)
        }
        depositAccountProcessorConfig.validateRequired()

        invoiceProcessor = InvoiceProcessor(this, invoiceProcessorConfig, txService)
        paymentProcessor = PaymentProcessor(this, paymentProcessorConfig, txService)
        depositAccountProcessor = DepositAccountProcessor(this, depositAccountProcessorConfig, txService)
    }

    constructor(
        coinsManager: CoinsManager,
        invoiceProcessor: InvoiceProcessor,
        paymentProcessor: PaymentProcessor,
        depositAccountProcessor: DepositAccountProcessor
    ) {
        this.coinsManager = coinsManager

        val threadPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        emitter = this.coinsManager.getAllEmitters().observeOn(Schedulers.from(threadPool))

        this.invoiceProcessor = invoiceProcessor
        this.paymentProcessor = paymentProcessor
        this.depositAccountProcessor = depositAccountProcessor
    }

    fun getBalance(currency: Currency): BigDecimal {
        return coinsManager.getBalance(currency)
    }

    fun sendPayment(currency: Currency, amount: BigDecimal, address: String, tag: String? = null): String {
        val payment = paymentProcessor.createPayment(currency, amount, address, tag)
        val tx = coinsManager.sendPayment(currency, amount, address, tag)
        paymentProcessor.updatePayment(payment.id, tx)
        return payment.id
    }

    fun createInvoice(currency: Currency, amount: BigDecimal): String {
        val tag = coinsManager.getTag(currency)
        val address = coinsManager.getAddress(currency)
        val invoice = invoiceProcessor.createInvoice(currency, amount, address, tag)
        return invoice.id
    }

    fun getPayment(id: String): Payment? = paymentProcessor.getPayment(id)

    fun getInvoice(id: String): Invoice? = invoiceProcessor.getInvoice(id)

    fun getTx(currency: Currency, txid: TxId): Tx {
        return coinsManager.getTx(currency, txid)
    }

    fun getTxs(currency: Currency, txids: List<TxId>): List<Tx> {
        return coinsManager.getTxs(currency, txids)
    }

    fun createNewAccount(id: String, currencies: List<Currency>) {
        depositAccountProcessor.createNewAccount(id, currencies)
    }

    fun createNewAddress(id: String, currency: Currency): String {
        val address = coinsManager.getAddress(currency)
        depositAccountProcessor.createNewAddress(id, currency, address)
        return address
    }

    fun getDepositAccount(id: String): DepositAccount? = depositAccountProcessor.getDepositAccount(id)

    fun getAllTx(id: String, currency: Currency): List<Tx> {
        return depositAccountProcessor.getAllTx(id, currency)
    }

    fun getLastTxToAddress(id: String, currency: Currency, address: String, amount: Int): List<Tx> {
        return depositAccountProcessor.getLastTxToAddress(id, currency, address, amount)
    }

    fun subscribe(observer: Observer<Tx>) {
        emitter.subscribe(observer)
    }
}