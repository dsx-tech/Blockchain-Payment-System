package dsx.bps.crypto.eth.datamodel

import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.RemoteCall
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger

/**
 *
 * Auto generated code.
 *
 * **Do not modify!**
 *
 * Please use the [web3j command line tools](https://docs.web3j.io/command_line.html),
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * [codegen module](https://github.com/web3j/web3j/tree/master/codegen) to update.
 *
 *
 * Generated with web3j version 4.5.5.
 */
class Proxy: Contract {

    @Deprecated("")
    protected constructor(
        contractAddress: String?,
        web3j: Web3j,
        credentials: Credentials,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit) {
    }

    protected constructor(
        contractAddress: String?,
        web3j: Web3j,
        credentials: Credentials,
        contractGasProvider: ContractGasProvider
    ): super(BINARY, contractAddress, web3j, credentials, contractGasProvider) {
    }

    @Deprecated("")
    protected constructor(
        contractAddress: String?,
        web3j: Web3j,
        transactionManager: TransactionManager,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ): super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit) {
    }

    protected constructor(
        contractAddress: String?,
        web3j: Web3j,
        transactionManager: TransactionManager,
        contractGasProvider: ContractGasProvider
    ): super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider) {
    }

    companion object {
        private val BINARY =
            "6080604052348015600f57600080fd5b50600080546001600160a01b031916331790556078806100306000396000f3fe60806040526000805460405134926001600160a01b039092169183156108fc02918491818181858888f19350505050158015603e573d6000803e3d6000fd5b505000fea26469706673582212201ce70914dff290dc5c6bd8b69a42755142791c1c094430f1a65ac937e2b0388364736f6c63430006040033"

        @Deprecated("")
        fun load(
            contractAddress: String,
            web3j: Web3j,
            credentials: Credentials,
            gasPrice: BigInteger,
            gasLimit: BigInteger
        ): Proxy {
            return Proxy(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        @Deprecated("")
        fun load(
            contractAddress: String,
            web3j: Web3j,
            transactionManager: TransactionManager,
            gasPrice: BigInteger,
            gasLimit: BigInteger
        ): Proxy {
            return Proxy(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }

        fun load(
            contractAddress: String,
            web3j: Web3j,
            credentials: Credentials,
            contractGasProvider: ContractGasProvider
        ): Proxy {
            return Proxy(contractAddress, web3j, credentials, contractGasProvider)
        }

        fun load(
            contractAddress: String,
            web3j: Web3j,
            transactionManager: TransactionManager,
            contractGasProvider: ContractGasProvider
        ): Proxy {
            return Proxy(contractAddress, web3j, transactionManager, contractGasProvider)
        }

        fun deploy(
            web3j: Web3j,
            credentials: Credentials,
            contractGasProvider: ContractGasProvider
        ): RemoteCall<Proxy> {
            return Contract.deployRemoteCall(
                Proxy::class.java,
                web3j,
                credentials,
                contractGasProvider,
                BINARY,
                ""
            )
        }

        fun deploy(
            web3j: Web3j,
            transactionManager: TransactionManager,
            contractGasProvider: ContractGasProvider
        ): RemoteCall<Proxy> {
            return Contract.deployRemoteCall(
                Proxy::class.java,
                web3j,
                transactionManager,
                contractGasProvider,
                BINARY,
                ""
            )
        }

        @Deprecated("")
        fun deploy(
            web3j: Web3j,
            credentials: Credentials,
            gasPrice: BigInteger,
            gasLimit: BigInteger
        ): RemoteCall<Proxy> {
            return Contract.deployRemoteCall(
                Proxy::class.java,
                web3j,
                credentials,
                gasPrice,
                gasLimit,
                BINARY,
                ""
            )
        }

        @Deprecated("")
        fun deploy(
            web3j: Web3j,
            transactionManager: TransactionManager,
            gasPrice: BigInteger,
            gasLimit: BigInteger
        ): RemoteCall<Proxy> {
            return Contract.deployRemoteCall(
                Proxy::class.java,
                web3j,
                transactionManager,
                gasPrice,
                gasLimit,
                BINARY,
                ""
            )
        }
    }
}
