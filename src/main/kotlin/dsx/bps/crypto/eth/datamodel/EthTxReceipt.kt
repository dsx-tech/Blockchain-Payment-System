package dsx.bps.crypto.eth.datamodel
import com.google.gson.annotations.SerializedName

data class EthTxReceipt (
    @SerializedName("transactionHash")
    val transactionHash: String,
    @SerializedName("transactionIndex")
    val transactionIndex: String,
    @SerializedName("blockHash")
    val blockHash: String,
    @SerializedName("blockNumber")
    val blockNumber: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("cumulativeGasUsed")
    val cumulativeGasUsed: String,
    @SerializedName("gasUsed")
    val gasUsed: String,
    @SerializedName("contractAddress")
    val contractAddress: String,
    @SerializedName("logs")
    val logs: Array<String>,
    @SerializedName("logsBloom")
    val logsBloom: String,
    @SerializedName("status")
    val status: String
)