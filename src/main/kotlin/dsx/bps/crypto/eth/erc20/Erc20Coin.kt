package dsx.bps.crypto.eth.erc20

import com.uchuhimo.konf.Config
import dsx.bps.DBservices.core.TxService
import dsx.bps.config.currencies.EthConfig
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.eth.EthManager
import dsx.bps.crypto.eth.erc20.datamodel.Erc20Contract
import dsx.bps.crypto.eth.ethereum.datamodel.EthAccount
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

class Erc20Coin(token: Currency, conf: Config, txServ: TxService) :
    EthManager(conf, txServ) {
    override val currency : Currency = token
    override val connector: Erc20Rpc
    override  val explorer: Erc20Explorer
    val contract: Erc20Contract
    val accounts = mutableMapOf<String, Pair<EthAccount,Boolean>>()


    init {
        val config = conf
        val credentials = WalletUtils.loadCredentials(systemAccount.password, systemAccount.wallet)
        val contractAddress = config[EthConfig.Erc20.tokens]
            .getValue(token.name)
        connector = Erc20Rpc(commonConnector, contractAddress, credentials)
        val ct = ethService.getContractByAddress(contractAddress)
        if (ct == null) {
           contract = Erc20Contract(contractAddress, connector.decimals(), connector.owner())
           ethService.addContract(contract)
        } else {
            contract = ct
        }
        explorer = Erc20Explorer(this, connector.token, frequency, txService)
    }

    fun getTransactionByHash(hash: String) = connector.ethRpc.getTransactionByHash(hash)

    override fun getBalance(): BigDecimal = convertAmountToDecimal(connector.balanceOf(systemAccount.address))

    override fun getAddress() : String {
        val newAccount = connector.ethRpc.generateWalletFile(defaultPasswordForNewAddresses, walletsDir)
        accounts[newAccount.address] = Pair(newAccount, false)
        return newAccount.address
    }

    override fun getTx(txid: TxId): Tx {
        val erc20Tx = ethService.getTokensTransfer(txid.hash, contract.owner, contract.address)
        return constructTx(txid.hash, erc20Tx.to, erc20Tx.amount)
    }

    fun constructTx(ethTxHash: String, to: String, amount: BigDecimal, currency: Currency = this.currency): Tx {
        val ethTx = connector.ethRpc.getTransactionByHash(ethTxHash)
        return object: Tx {
            override fun currency() = currency

            override fun hash() = ethTxHash

            override fun amount() = amount

            override fun destination() = to

            override fun fee(): BigDecimal {
                return if (this.status() == TxStatus.VALIDATING) {
                    Convert.fromWei(ethTx.gasPrice.multiply(ethTx.gas).toBigDecimal(), Convert.Unit.ETHER)
                } else {
                    Convert.fromWei(ethTx.gasPrice
                        .multiply(connector.ethRpc.getTransactionReceiptByHash(ethTx.hash).gasUsed).toBigDecimal(),
                        Convert.Unit.ETHER)
                }
            }

            override fun status(): TxStatus {
                val latestBlock = connector.ethRpc.getLatestBlock()
                if (connector.ethRpc.getTransactionByHash(ethTxHash).blockHash == null) {
                    return TxStatus.VALIDATING
                } else {
                    val conf = latestBlock.number - ethTx.blockNumber
                    if (conf < confirmations.toBigInteger()) {
                        return TxStatus.VALIDATING
                    } else {
                        return TxStatus.CONFIRMED
                    }
                }
            }
        }
    }

    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
        val intAmount = convertAmountToInteger(amount)
        val resultHash = connector.transfer(address, intAmount).transactionHash
        val tx = connector.ethRpc.getTransactionByHash(resultHash)
        val transaction = constructTx(resultHash, address, amount)

        val txs = txService.add(transaction.status(), transaction.destination(),"",
            transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())
        ethService.addEthTransaction(systemAccount.address, tx.nonce.toLong() , txs, connector.token.contractAddress)

        return transaction
    }

    fun convertAmountToDecimal(amount: BigInteger) : BigDecimal {
        return amount.toBigDecimal().divide(Math.pow(10.0, contract.decimals.toDouble()).toBigDecimal())
    }

    fun convertAmountToInteger(amount: BigDecimal) : BigInteger {
        return (amount * Math.pow(10.0, contract.decimals.toDouble()).toBigDecimal()).toBigInteger()
    }

    fun transferFrom(holder: String, amount: BigDecimal): TransactionReceipt {
        val intAmount = convertAmountToInteger(amount)
        return connector.transferFrom(holder, systemAccount.address, intAmount)
    }

    fun transferAllFundsFrom(holder: String): Pair<TransactionReceipt, BigDecimal> {
        val balance = connector.balanceOf(holder)
        return Pair(connector.transferFrom(holder, systemAccount.address, balance), convertAmountToDecimal(balance))
    }

    fun approveFrom(owner: EthAccount): TransactionReceipt {
        val amount = BigInteger("2").pow(256).subtract(BigInteger.ONE)
            .divide(BigInteger.TEN.pow(contract.decimals))
        return connector.approveSystem(systemAccount.address, owner, amount)
    }

}