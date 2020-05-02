package dsx.bps.connector.rpc

data class RpcResponse(
    val json: String,
    val id: String? = null
)