package dsx.bps.connection.rpc

import com.google.gson.Gson
import dsx.bps.connection.Connector
import dsx.bps.exception.connector.BpsConnectorException
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory
import kotlin.random.Random

abstract class JsonRpcHttpClient : Connector {

    protected var rpcURL: URL
    protected var auth: String?

    var hostnameVerifier: HostnameVerifier? = null
    var sslSocketFactory: SSLSocketFactory? = null
    protected val headers = mutableMapOf<String, String>()

    protected open val gson: Gson = Gson()
    protected val charset = Charsets.ISO_8859_1

    constructor(url: String): this(URL(url))

    constructor(url: URL) {
        rpcURL = URI(url.protocol, null, url.host, url.port, url.path, url.query, null).toURL()
        auth = url.userInfo
            ?.toByteArray(charset)
            ?.let { Base64.getEncoder().encodeToString(it) }
    }

    fun query(method: String, vararg params: Any): Any? {
        return constructRequest(method, *params)
            .let(::execute)
            .let(::parseResponse)
    }

    protected open fun constructRequest(method: String, vararg params: Any): RpcRequest {
        val id = Random.nextInt().toString()
        val json = gson.toJson(
            mapOf(
                "method" to method,
                "params" to params,
                "id" to id
            )
        )
        return RpcRequest(rpcURL, json, id)
    }

    private fun connect(url: URL): HttpURLConnection {
        val conn = url.openConnection() as HttpURLConnection
        conn.doInput = true
        conn.doOutput = true
        conn.requestMethod = "POST"
        if (conn is HttpsURLConnection) {
            if (hostnameVerifier != null)
                conn.hostnameVerifier = hostnameVerifier
            if (sslSocketFactory != null)
                conn.sslSocketFactory = sslSocketFactory
        }

        headers.forEach { (k, v) -> conn.setRequestProperty(k, v) }
        return conn
    }

    private fun execute(request: RpcRequest): RpcResponse {
        val conn = connect(request.url)
        val bytes = request.json.toByteArray(charset)
        conn.outputStream.use { it.write(bytes) }

        if (conn.responseCode != 200) {
            val errorStream = conn.errorStream
            throw BpsConnectorException(
                "$request failed\n" +
                        "Code: ${conn.responseCode}\n" +
                        "Message: ${conn.responseMessage}\n" +
                        "Response: ${errorStream.use { it.readBytes().toString(charset) }}"
            )
        }

        val response = conn.inputStream
            .bufferedReader(charset)
            .use { it.readText() }
        return RpcResponse(response, request.id)
    }

    protected open fun parseResponse(response: RpcResponse): Any? {
        val obj = gson.fromJson(response.json, Map::class.java)

        if (response.id != obj["id"])
            throw BpsConnectorException("Wrong response id: sent ${response.id}, received ${obj["id"]}")

        if (obj["error"] != null) {
            val error = obj["error"] as Map<*, *>
            throw BpsConnectorException("RPC error: code ${error["code"]}, message: ${error["message"]}")
        }

        return obj["result"]
    }
}