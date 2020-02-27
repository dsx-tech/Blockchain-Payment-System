package dsx.bps.crypto.xrp.datamodel

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import dsx.bps.core.datamodel.Currency
import java.lang.reflect.Type
import java.math.BigDecimal

data class XrpAmount(
    val issuer: String = "",
    val value: BigDecimal = BigDecimal.ZERO,
    val currency: String = Currency.XRP.name
) {

    companion object {
        class XrpAmountDeserializer: JsonDeserializer<XrpAmount> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): XrpAmount? {

                if (json == null)
                    return null

                if (json.isJsonPrimitive)
                    return XrpAmount(value = json.asBigDecimal)

                val obj = json.asJsonObject
                return XrpAmount(
                    obj["issuer"].asString,
                    obj["value"].asBigDecimal,
                    obj["currency"].asString
                )

            }
        }
    }
}