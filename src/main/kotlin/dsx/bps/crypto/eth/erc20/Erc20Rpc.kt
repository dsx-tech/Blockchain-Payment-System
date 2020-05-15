package dsx.bps.crypto.eth.erc20

import dsx.bps.connector.Connector
import dsx.bps.crypto.eth.CommonConnector
import dsx.bps.crypto.eth.erc20.datamodel.ERC20
import dsx.bps.crypto.eth.ethereum.datamodel.EthAccount
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger


class Erc20Rpc(val ethRpc: CommonConnector,val contractAddress: String, credentials: Credentials): Connector() {
    val token = ERC20.load(contractAddress, ethRpc.web3j, credentials, DefaultGasProvider())

    fun decimals() : Int {
        return token.decimals().send().toInt()
    }

    fun owner() : String {
        return  token.owner().send()
    }

    fun balanceOf(address: String): BigInteger {
        return  token.balanceOf(address).send()
    }

    fun transfer(to: String, amount: BigInteger) : TransactionReceipt {
        return token.transfer(to, amount).send()
    }

    fun approve(delegate: String, numTokens: BigInteger): TransactionReceipt {
        return token.approve(delegate, numTokens).send()
    }

    fun transferFrom(holder: String, buyer: String, numTokens: BigInteger) : TransactionReceipt {
        return  token.transferFrom(holder, buyer, numTokens).send()
    }

    fun approveSystem(systemAddress: String, owner: EthAccount, amount: BigInteger): TransactionReceipt {
        val ownerCredentials = WalletUtils.loadCredentials(owner.password, owner.wallet)
        val ownerToken = ERC20.load(contractAddress, ethRpc.web3j, ownerCredentials, DefaultGasProvider())
        return ownerToken.approve(systemAddress, amount).send()
    }
}