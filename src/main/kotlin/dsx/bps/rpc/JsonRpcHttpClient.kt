package dsx.bps.rpc

import com.google.gson.Gson
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory
import kotlin.random.Random

open class JsonRpcHttpClient: JsonRpcClient {

    private val rpcURL: URL
    protected val auth: String?

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

    override fun query(method: String, vararg params: Any): Any? {
        val conn = connect()
        val id = Random.nextInt().toString()
        val r = constructRequest(method, id, *params)

        conn.outputStream.use {  it.write(r) }
        if (conn.responseCode != 200) {
            val errorStream = conn.errorStream
            throw RuntimeException("RPC query ${r.toString(charset)} failed\n" +
                    "Code: ${conn.responseCode}\n" +
                    "Message: ${conn.responseMessage}\n" +
                    "Response: ${errorStream.use { it.readBytes().toString(charset) }}")
        }

        return parseResponse(conn.inputStream, id)
    }

    private fun connect(): HttpURLConnection {
        val conn = rpcURL.openConnection() as HttpURLConnection
        conn.doInput = true
        conn.doOutput = true
        conn.requestMethod = "POST"
        if (conn is HttpsURLConnection) {
            if (hostnameVerifier != null)
                conn.hostnameVerifier = hostnameVerifier
            if (sslSocketFactory != null)
                conn.sslSocketFactory = sslSocketFactory
        }

        headers.forEach { k, v -> conn.setRequestProperty(k, v) }
        return conn
    }

    private fun constructRequest(method: String, id: String, vararg params: Any): ByteArray {
        return gson.toJson( mapOf(
            "method" to method,
            "params" to params,
            "id"     to id
        )).toByteArray(Charset.forName("ISO8859-1"))
    }

    protected open fun parseResponse(input: InputStream, id: String): Any? {
        val r = input.use { it.readBytes().toString(charset) }
        val json = gson.fromJson(r, Map::class.java)

        if (id != json["id"])
            throw RuntimeException("Wrong response id: sent $id, received ${json["id"]}")

        if (json["error"] != null) {
            val error = json["error"] as Map<*, *>
            throw RuntimeException("RPC error: code ${error["code"]}, message: ${error["message"]}")
        }

        return json["result"]
    }
}