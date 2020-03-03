package dsx.bps.connection.rpc

data class RpcResponse(
    val json: String,
    val id: String? = null
)