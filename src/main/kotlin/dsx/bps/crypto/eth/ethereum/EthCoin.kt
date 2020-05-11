package dsx.bps.crypto.eth.ethereum

import com.uchuhimo.konf.Config
import dsx.bps.DBservices.Datasource
import dsx.bps.DBservices.core.TxService
import dsx.bps.core.datamodel.Currency
import dsx.bps.core.datamodel.Tx
import dsx.bps.core.datamodel.TxId
import dsx.bps.core.datamodel.TxStatus
import dsx.bps.crypto.eth.EthManager
import dsx.bps.crypto.eth.ethereum.datamodel.EthAccount
import org.web3j.protocol.core.methods.response.EthBlock.Block
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

class EthCoin(conf: Config, datasource: Datasource, txServ: TxService) : EthManager(conf, datasource, txServ) {
    override val currency = Currency.ETH
    override val explorer: EthExplorer
    override val connector = commonConnector
    val accounts = mutableListOf<EthAccount>()

    init {
        explorer = EthExplorer(this, frequency, datasource, txService)
    }


    /**
     * @return account balance in ether.
     */
    override fun getBalance(): BigDecimal = connector.getBalance(systemAccount.address)

    /**
     * Get new smart-contract address. This is an alternative to getting an address by creating a new wallet.
     * @return smart-contract address.
     */
    fun getSmartAddress(): String{
        return connector.generateSmartWallet(systemAccount.wallet, systemAccount.password).address
    }

    /**
     * @return new account address
     */
    override fun getAddress() : String {
        synchronized(accounts)
        {
            val newAccount = connector.generateWalletFile(defaultPasswordForNewAddresses, walletsDir)
            accounts.add(newAccount)
            return newAccount.address
        }
    }

    /**
     * @param txid TxId object ( {hash : String, index : Int} )
     * @return Tx oject - generalized transaction template in the system
     */
    override fun getTx(txid: TxId): Tx {
        val ethTx = connector.getTransactionByHash(txid.hash)
        return constructTx(ethTx)
    }

    /**
     * @param ethTx EthTx - ether transaction template
     * @return Tx oject - generalized transaction template in the system
     */
    fun constructTx(ethTx: Transaction): Tx {
        return object: Tx {
            override fun currency() = Currency.ETH

            override fun hash() = ethTx.hash

            override fun amount() = Convert.fromWei(ethTx.value.toBigDecimal(), Convert.Unit.ETHER)

            override fun destination(): String {
                return if (ethTx.to == null) {
                    "0x0"
                } else {
                    ethTx.to
                }
            }

            override fun fee(): BigDecimal {
                return if (this.status() == TxStatus.VALIDATING) {
                    Convert.fromWei(ethTx.gasPrice.multiply(ethTx.gas).toBigDecimal(), Convert.Unit.ETHER)
                } else {
                    Convert.fromWei(ethTx.gasPrice
                        .multiply(connector.getTransactionReceiptByHash(ethTx.hash).gasUsed).toBigDecimal(),
                        Convert.Unit.ETHER)
                }
            }

            override fun status(): TxStatus {
                val latestBlock = connector.getLatestBlock()
                if (ethTx.blockHash == null) {
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

    /**
     * @param amount amount of payment in ether
     * @param address address of the recipient
     * @return Tx oject - generalized transaction template in the system
     */
    override fun sendPayment(amount: BigDecimal, address: String, tag: String?): Tx {
        val resultHash= send(EthAccount(systemAccount.address, systemAccount.wallet, systemAccount.password), address, amount)
        val ethTx = connector.getTransactionByHash(resultHash)
        val transaction = constructTx(ethTx)

        val txs = txService.add(transaction.status(), transaction.destination(),"",
            transaction.amount(), transaction.fee(), transaction.hash(), transaction.index(), transaction.currency())
        ethService.addEthTransaction(systemAccount.address, ethTx.nonce.toLong(), txs, "")
        return constructTx(connector.getTransactionByHash(resultHash))
    }

    fun getLatestBlock(): Block {
        return connector.getLatestBlock()
    }

    fun getBlockByHash(hash: String): Block {
        return connector.getBlockByHash(hash)
    }

    fun getGasPrice(): BigInteger {
        return connector.getGasPrice()
    }

    fun getAccountByAddress(address: String): EthAccount {
        return accounts.find { it.address == address }!!
    }

    @Deprecated("only for tests")
    override fun kill(){
        this.explorer.kill()
    }

    @Deprecated("only for tests")
    override fun clearDb() {
        this.ethService.delete()
    }
}