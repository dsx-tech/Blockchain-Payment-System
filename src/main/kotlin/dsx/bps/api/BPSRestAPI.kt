package dsx.bps.api

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import dsx.bps.config.RestAPIConfig
import dsx.bps.config.currencies.EnabledCurrenciesConfig
import dsx.bps.core.datamodel.Currency
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.math.BigDecimal


private val configPath = ClassLoader.getSystemResource("BpsConfig.yaml").toURI().path
private val blockchainPaymentSystemAPI = BlockchainPaymentSystemAPI(configPath)
private val enabledCurrencies: Map<String, Currency> = run {
    val config = with(Config()) {
        addSpec(EnabledCurrenciesConfig)
        from.yaml.file(configPath)
    }
    config.validateRequired()
    val enabledCurrencies = config[EnabledCurrenciesConfig.coins]
    val enabledCurrenciesMap: MutableMap<String, Currency> = mutableMapOf()
    for (currency in enabledCurrencies) {
        enabledCurrenciesMap[currency.name] = currency
    }
    enabledCurrenciesMap.toMap()
}
private val restApiConfig: Config = run {
    val config = with(Config()) {
        addSpec(RestAPIConfig)
        from.yaml.file(configPath)
    }
    config.validateRequired()
}

fun main() {
    embeddedServer(
        Netty,
        restApiConfig[RestAPIConfig.port],
        restApiConfig[RestAPIConfig.host],
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(StatusPages) {
        exception<Throwable> { cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
        }
    }
    install(CallLogging)
    routing {
        get("/balance/{currency}") {
            val currentCurrencyName: String? = call.parameters["currency"]
            if (currentCurrencyName == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Currency is not specified."
                )
            } else {
                if (enabledCurrencies.contains(currentCurrencyName)) {
                    val currency = enabledCurrencies.getValue(currentCurrencyName)
                    call.respond(
                        mapOf<String, String>(
                            "currency" to currency.name,
                            "balance" to blockchainPaymentSystemAPI.getBalance(currency)
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Currency $currentCurrencyName is not enable."
                    )
                }
            }
        }

        get("/invoice/{id}") {
            val invoiceId = call.parameters["id"]
            if (invoiceId == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Invoice id is not specified."
                )
            } else {
                val invoice = blockchainPaymentSystemAPI.getInvoice(invoiceId)
                if (invoice == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "Invoice with id $invoiceId was not found."
                    )
                } else {
                    call.respond(invoice)
                }
            }
        }

        get("/payment/{id}") {
            val paymentId = call.parameters["id"]
            if (paymentId == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Payment id is not specified."
                )
            } else {
                val payment = blockchainPaymentSystemAPI.getPayment(paymentId)
                if (payment == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "Payment with id $paymentId was not found."
                    )
                } else {
                    call.respond(payment)
                }
            }
        }

        post("/invoice") {
            val data: Map<String, String> = call.receive()
            val currentCurrencyName: String? = data["currency"]
            val amount: BigDecimal? = data["amount"]?.toBigDecimalOrNull()
            if (currentCurrencyName == null || !enabledCurrencies.contains(currentCurrencyName)) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Currency is incorrect."
                )
            } else if (amount == null || amount < BigDecimal.ZERO) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Amount is incorrect."
                )
            } else {
                call.respond(
                    blockchainPaymentSystemAPI.createInvoice(
                        Currency.GRM,
                        BigDecimal.ONE
                    )
                )
            }
        }

        post("/payment") {
            val data: Map<String, String> = call.receive()
            val currentCurrencyName: String? = data["currency"]
            val amount: BigDecimal? = data["amount"]?.toBigDecimalOrNull()
            val address: String? = data["address"]
            val tag: String? = data["tag"]
            if (currentCurrencyName == null || !enabledCurrencies.contains(currentCurrencyName)) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Currency is incorrect."
                )
            } else if (amount == null || amount < BigDecimal.ZERO) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Amount is incorrect."
                )
            } else if (address == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Address is incorrect."
                )
            } else {
                call.respond(
                    blockchainPaymentSystemAPI.sendPayment(
                        enabledCurrencies.getValue(currentCurrencyName),
                        amount, address, tag
                    )
                )
            }
        }

    }
}