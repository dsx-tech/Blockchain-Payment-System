package dsx.bps.rpc

import java.net.URL

data class RpcRequest(
    val url: URL,
    val json: String,
    val id: String? = null
)