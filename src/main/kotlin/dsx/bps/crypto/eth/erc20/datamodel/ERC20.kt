package dsx.bps.crypto.eth.erc20.datamodel

import io.reactivex.Flowable
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger
import java.util.*

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
 * Generated with web3j version 4.4.0.
 */
class ERC20 : Contract {

    @Deprecated("")
    protected constructor(
        contractAddress: String?,
        web3j: Web3j,
        credentials: Credentials,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ) : super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit) {
    }

    protected constructor(
        contractAddress: String?,
        web3j: Web3j,
        credentials: Credentials,
        contractGasProvider: ContractGasProvider
    ) : super(BINARY, contractAddress, web3j, credentials, contractGasProvider) {
    }

    @Deprecated("")
    protected constructor(
        contractAddress: String?,
        web3j: Web3j,
        transactionManager: TransactionManager,
        gasPrice: BigInteger,
        gasLimit: BigInteger
    ) : super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit) {
    }

    protected constructor(
        contractAddress: String?,
        web3j: Web3j,
        transactionManager: TransactionManager,
        contractGasProvider: ContractGasProvider
    ) : super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider) {
    }

    fun getApprovalEvents(transactionReceipt: TransactionReceipt): List<ApprovalEventResponse> {
        val valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt)
        val responses = ArrayList<ApprovalEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = ApprovalEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.tokenOwner = eventValues.indexedValues[0].value as String
            typedResponse.spender = eventValues.indexedValues[1].value as String
            typedResponse.tokens = eventValues.nonIndexedValues[0].value as BigInteger
            responses.add(typedResponse)
        }
        return responses
    }

    fun approvalEventFlowable(filter: EthFilter): Flowable<ApprovalEventResponse> {
        return web3j.ethLogFlowable(filter).map { log ->
            val eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log)
            val typedResponse = ApprovalEventResponse()
            typedResponse.log = log
            typedResponse.tokenOwner = eventValues.indexedValues[0].value as String
            typedResponse.spender = eventValues.indexedValues[1].value as String
            typedResponse.tokens = eventValues.nonIndexedValues[0].value as BigInteger
            typedResponse
        }
    }

    fun approvalEventFlowable(
        startBlock: DefaultBlockParameter,
        endBlock: DefaultBlockParameter
    ): Flowable<ApprovalEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT))
        return approvalEventFlowable(filter)
    }

    fun getTransferEvents(transactionReceipt: TransactionReceipt): List<TransferEventResponse> {
        val valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt)
        val responses = ArrayList<TransferEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = TransferEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.from = eventValues.indexedValues[0].value as String
            typedResponse.to = eventValues.indexedValues[1].value as String
            typedResponse.tokens = eventValues.nonIndexedValues[0].value as BigInteger
            responses.add(typedResponse)
        }
        return responses
    }

    fun transferEventFlowable(filter: EthFilter): Flowable<TransferEventResponse> {
        return web3j.ethLogFlowable(filter).map { log ->
            val eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log)
            val typedResponse = TransferEventResponse()
            typedResponse.log = log
            typedResponse.from = eventValues.indexedValues[0].value as String
            typedResponse.to = eventValues.indexedValues[1].value as String
            typedResponse.tokens = eventValues.nonIndexedValues[0].value as BigInteger
            typedResponse
        }
    }

    fun transferEventFlowable(
        startBlock: DefaultBlockParameter,
        endBlock: DefaultBlockParameter
    ): Flowable<TransferEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT))
        return transferEventFlowable(filter)
    }

    fun allowance(holder: String, delegate: String): RemoteCall<BigInteger> {
        val function = org.web3j.abi.datatypes.Function(FUNC_ALLOWANCE,
            Arrays.asList<Type<*>>(
                Address(holder),
                Address(delegate)
            ),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Uint256>() {

            })
        )
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    fun approve(delegate: String, numTokens: BigInteger): RemoteCall<TransactionReceipt> {
        val function = org.web3j.abi.datatypes.Function(
            FUNC_APPROVE,
            Arrays.asList<Type<*>>(
                Address(delegate),
                Uint256(numTokens)
            ),
            emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun balanceOf(tokenOwner: String): RemoteCall<BigInteger> {
        val function = org.web3j.abi.datatypes.Function(FUNC_BALANCEOF,
            Arrays.asList<Type<*>>(Address(tokenOwner)),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Uint256>() {

            })
        )
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    fun decimals(): RemoteCall<BigInteger> {
        val function = org.web3j.abi.datatypes.Function(FUNC_DECIMALS,
            Arrays.asList(),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Uint8>() {

            })
        )
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    fun name(): RemoteCall<String> {
        val function = org.web3j.abi.datatypes.Function(FUNC_NAME,
            Arrays.asList(),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Utf8String>() {

            })
        )
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun owner(): RemoteCall<String> {
        val function = org.web3j.abi.datatypes.Function(FUNC_OWNER,
            Arrays.asList(),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Address>() {

            })
        )
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun symbol(): RemoteCall<String> {
        val function = org.web3j.abi.datatypes.Function(FUNC_SYMBOL,
            Arrays.asList(),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Utf8String>() {

            })
        )
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun totalSupply(): RemoteCall<BigInteger> {
        val function = org.web3j.abi.datatypes.Function(FUNC_TOTALSUPPLY,
            Arrays.asList(),
            Arrays.asList<TypeReference<*>>(object : TypeReference<Uint256>() {

            })
        )
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    fun transfer(receiver: String, numTokens: BigInteger): RemoteCall<TransactionReceipt> {
        val function = org.web3j.abi.datatypes.Function(
            FUNC_TRANSFER,
            Arrays.asList<Type<*>>(
                Address(receiver),
                Uint256(numTokens)
            ),
            emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    fun transferFrom(holder: String, buyer: String, numTokens: BigInteger): RemoteCall<TransactionReceipt> {
        val function = org.web3j.abi.datatypes.Function(
            FUNC_TRANSFERFROM,
            Arrays.asList<Type<*>>(
                Address(holder),
                Address(buyer),
                Uint256(numTokens)
            ),
            emptyList()
        )
        return executeRemoteCallTransaction(function)
    }

    class ApprovalEventResponse {
        var log: Log? = null

        var tokenOwner: String? = null

        var spender: String? = null

        var tokens: BigInteger? = null
    }

    class TransferEventResponse {
        var log: Log? = null

        var from: String? = null

        var to: String? = null

        var tokens: BigInteger? = null
    }

    companion object {
        private val BINARY =
            "60806040523480156200001157600080fd5b506040516200142438038062001424833981810160405260208110156200003757600080fd5b8101908080519060200190929190505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000601260ff16600a0a60c80290508160038190555080601260ff16600a0a6003540203600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555060006200011b6040518060600160405280602a8152602001620013fa602a91396200016a60201b60201c565b905081600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550505050620003d7565b6000606082905060008090506000806000600290505b602a811015620003ca57610100840293508481815181106200019e57fe5b602001015160f81c60f81b60f81c60ff169250846001820181518110620001c157fe5b602001015160f81c60f81b60f81c60ff16915060618373ffffffffffffffffffffffffffffffffffffffff161015801562000213575060668373ffffffffffffffffffffffffffffffffffffffff1611155b156200022557605783039250620002c4565b60418373ffffffffffffffffffffffffffffffffffffffff161015801562000264575060468373ffffffffffffffffffffffffffffffffffffffff1611155b156200027657603783039250620002c3565b60308373ffffffffffffffffffffffffffffffffffffffff1610158015620002b5575060398373ffffffffffffffffffffffffffffffffffffffff1611155b15620002c2576030830392505b5b5b60618273ffffffffffffffffffffffffffffffffffffffff161015801562000303575060668273ffffffffffffffffffffffffffffffffffffffff1611155b156200031557605782039150620003b4565b60418273ffffffffffffffffffffffffffffffffffffffff161015801562000354575060468273ffffffffffffffffffffffffffffffffffffffff1611155b156200036657603782039150620003b3565b60308273ffffffffffffffffffffffffffffffffffffffff1610158015620003a5575060398273ffffffffffffffffffffffffffffffffffffffff1611155b15620003b2576030820391505b5b5b8160108402018401935060028101905062000180565b5082945050505050919050565b61101380620003e76000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c806370a082311161006657806370a08231146102545780638da5cb5b146102ac57806395d89b41146102f6578063a9059cbb14610379578063dd62ed3e146103df5761009e565b806306fdde03146100a3578063095ea7b31461012657806318160ddd1461018c57806323b872dd146101aa578063313ce56714610230575b600080fd5b6100ab610457565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100eb5780820151818401526020810190506100d0565b50505050905090810190601f1680156101185780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101726004803603604081101561013c57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610490565b604051808215151515815260200191505060405180910390f35b610194610582565b6040518082815260200191505060405180910390f35b610216600480360360608110156101c057600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061058c565b604051808215151515815260200191505060405180910390f35b610238610ad4565b604051808260ff1660ff16815260200191505060405180910390f35b6102966004803603602081101561026a57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610ad9565b6040518082815260200191505060405180910390f35b6102b4610b22565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6102fe610b47565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561033e578082015181840152602081019050610323565b50505050905090810190601f16801561036b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103c56004803603604081101561038f57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610b80565b604051808215151515815260200191505060405180910390f35b610441600480360360408110156103f557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610f0b565b6040518082815260200191505060405180910390f35b6040518060400160405280600a81526020017f45524332305f436f696e0000000000000000000000000000000000000000000081525081565b600081600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040518082815260200191505060405180910390a36001905092915050565b6000600354905090565b6000806105a461271084610f9290919063ffffffff16565b9050600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546105f98285610fab90919063ffffffff16565b111561060457600080fd5b600260008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546106948285610fab90919063ffffffff16565b111561069f57600080fd5b610703816106f585600160008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610fc790919063ffffffff16565b610fc790919063ffffffff16565b600160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506107e7816107d985600260008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610fc790919063ffffffff16565b610fc790919063ffffffff16565b600260008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506108b983600160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610fab90919063ffffffff16565b600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508373ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef856040518082815260200191505060405180910390a36000811115610ac8576109dd81600160008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610fab90919063ffffffff16565b600160008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35b60019150509392505050565b601281565b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6040518060400160405280600581526020017f455243323000000000000000000000000000000000000000000000000000000081525081565b600080610b9861271084610f9290919063ffffffff16565b9050600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610bed8285610fab90919063ffffffff16565b1115610bf857600080fd5b610c5c81610c4e85600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610fc790919063ffffffff16565b610fc790919063ffffffff16565b600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550610cf183600160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610fab90919063ffffffff16565b600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508373ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef856040518082815260200191505060405180910390a36000811115610f0057610e1581600160008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610fab90919063ffffffff16565b600160008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35b600191505092915050565b6000600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b600080828481610f9e57fe5b0490508091505092915050565b600080828401905083811015610fbd57fe5b8091505092915050565b600082821115610fd357fe5b81830390509291505056fea265627a7a723158202d2e0cdcfd9e89a44c28e2b2b8a925e69373cb73faf83813566fd283e25661c964736f6c63430005110032305830434535393232354243443434374645414544363938454437353444333039464542413546433633"

        val FUNC_ALLOWANCE = "allowance"

        val FUNC_APPROVE = "approve"

        val FUNC_BALANCEOF = "balanceOf"

        val FUNC_DECIMALS = "decimals"

        val FUNC_NAME = "name"

        val FUNC_OWNER = "owner"

        val FUNC_SYMBOL = "symbol"

        val FUNC_TOTALSUPPLY = "totalSupply"

        val FUNC_TRANSFER = "transfer"

        val FUNC_TRANSFERFROM = "transferFrom"

        val APPROVAL_EVENT = Event("Approval",
            Arrays.asList<TypeReference<*>>(object : TypeReference<Address>(true) {

            }, object : TypeReference<Address>(true) {

            }, object : TypeReference<Uint256>() {

            })
        )

        val TRANSFER_EVENT = Event("Transfer",
            Arrays.asList<TypeReference<*>>(object : TypeReference<Address>(true) {

            }, object : TypeReference<Address>(true) {

            }, object : TypeReference<Uint256>() {

            })
        )

        @Deprecated("")
        fun load(
            contractAddress: String,
            web3j: Web3j,
            credentials: Credentials,
            gasPrice: BigInteger,
            gasLimit: BigInteger
        ): ERC20 {
            return ERC20(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        @Deprecated("")
        fun load(
            contractAddress: String,
            web3j: Web3j,
            transactionManager: TransactionManager,
            gasPrice: BigInteger,
            gasLimit: BigInteger
        ): ERC20 {
            return ERC20(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }

        fun load(
            contractAddress: String,
            web3j: Web3j,
            credentials: Credentials,
            contractGasProvider: ContractGasProvider
        ): ERC20 {
            return ERC20(contractAddress, web3j, credentials, contractGasProvider)
        }

        fun load(
            contractAddress: String,
            web3j: Web3j,
            transactionManager: TransactionManager,
            contractGasProvider: ContractGasProvider
        ): ERC20 {
            return ERC20(contractAddress, web3j, transactionManager, contractGasProvider)
        }

        fun deploy(
            web3j: Web3j,
            credentials: Credentials,
            contractGasProvider: ContractGasProvider,
            total: BigInteger
        ): RemoteCall<ERC20> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(Uint256(total)))
            return Contract.deployRemoteCall(
                ERC20::class.java,
                web3j,
                credentials,
                contractGasProvider,
                BINARY,
                encodedConstructor
            )
        }

        fun deploy(
            web3j: Web3j,
            transactionManager: TransactionManager,
            contractGasProvider: ContractGasProvider,
            total: BigInteger
        ): RemoteCall<ERC20> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(Uint256(total)))
            return Contract.deployRemoteCall(
                ERC20::class.java,
                web3j,
                transactionManager,
                contractGasProvider,
                BINARY,
                encodedConstructor
            )
        }

        @Deprecated("")
        fun deploy(
            web3j: Web3j,
            credentials: Credentials,
            gasPrice: BigInteger,
            gasLimit: BigInteger,
            total: BigInteger
        ): RemoteCall<ERC20> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(Uint256(total)))
            return Contract.deployRemoteCall(
                ERC20::class.java,
                web3j,
                credentials,
                gasPrice,
                gasLimit,
                BINARY,
                encodedConstructor
            )
        }

        @Deprecated("")
        fun deploy(
            web3j: Web3j,
            transactionManager: TransactionManager,
            gasPrice: BigInteger,
            gasLimit: BigInteger,
            total: BigInteger
        ): RemoteCall<ERC20> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(Uint256(total)))
            return Contract.deployRemoteCall(
                ERC20::class.java,
                web3j,
                transactionManager,
                gasPrice,
                gasLimit,
                BINARY,
                encodedConstructor
            )
        }
    }
}
