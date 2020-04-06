package dsx.bps.connector.rpc

import java.net.URL

data class RpcRequest(
    val url: URL,
    val json: String,
    val id: String? = null
)