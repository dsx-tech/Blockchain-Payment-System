package dsx.bps.DBservices.crypto

import dsx.bps.DBclasses.core.tx.TxEntity
import dsx.bps.DBclasses.crypto.erc20.Erc20ContractEntity
import dsx.bps.DBclasses.crypto.erc20.Erc20ContractTable
import dsx.bps.DBclasses.crypto.eth.EthAccountTable
import dsx.bps.DBclasses.crypto.eth.EthTxEntity
import dsx.bps.DBclasses.crypto.eth.EthTxTable
import dsx.bps.crypto.eth.erc20.datamodel.Erc20Contract
import dsx.bps.crypto.eth.erc20.datamodel.Erc20Tx
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigInteger

class EthService {

    init {
        transaction() {
            if (!EthTxTable.exists())
                SchemaUtils.create(EthTxTable)
            if (!EthAccountTable.exists())
                SchemaUtils.create(EthAccountTable)
            if (!Erc20ContractTable.exists())
                SchemaUtils.create(Erc20ContractTable)
        }
    }

    fun addEthTransaction(address: String, nonce: Long,
                          tx: TxEntity, contractAddress: String
    ): EthTxEntity {
        val newEthTxEntity = transaction {
            EthTxEntity.new {
                this.address = address
                this.contractAddress = contractAddress
                this.nonce = nonce
                this.tx = tx
            }
        }
        return newEthTxEntity
    }

    fun addContract(contract: Erc20Contract) {
        transaction {
            Erc20ContractEntity.new {
                this.contractAddress = contract.address
                this.decimals  = contract.decimals
                this.owner = contract.owner
            }
        }
    }

    fun getLatestNonce(address: String) : BigInteger? {
        val nonce = transaction{ EthTxEntity.all().filter { tx -> tx.address == address }.map { tx -> tx.nonce}.max()}
        return nonce?.toBigInteger() ?: return null
    }

    fun getTokensTransfer(txHash: String, owner: String, contractAddress: String): Erc20Tx {
        val txList = transaction { TxEntity.all().filter { it.hash == txHash } }
        val tx = txList.find { it.cryptoAddress.address !=  owner && it.cryptoAddress.address != contractAddress}
        return  Erc20Tx(txHash, tx!!.cryptoAddress.address, tx.amount)
    }

    fun getContractByAddress(contractAddress: String): Erc20Contract? {
        val contract = transaction { Erc20ContractEntity.all().filter { ct -> ct.contractAddress == contractAddress} }.firstOrNull()
        return if (contract == null) {
            null
        } else {
            Erc20Contract(contractAddress, contract.decimals, contract.owner)
        }

    }

    fun isNewTransaction(txHash: String, destination: String): Boolean {
        return transaction { TxEntity.all().filter { it.hash == txHash && it.cryptoAddress.address == destination } }.isEmpty()
    }

    fun delete() {
        transaction { EthTxEntity.all() .forEach {it.delete()} }
    }
}