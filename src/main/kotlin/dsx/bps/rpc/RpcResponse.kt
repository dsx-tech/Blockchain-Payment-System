package dsx.bps.rpc

data class RpcResponse(
    val json: String,
    val id: String? = null
)