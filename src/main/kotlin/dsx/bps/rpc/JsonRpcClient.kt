package dsx.bps.rpc

interface JsonRpcClient {

    fun query(method: String, vararg params: Any): Any?
}