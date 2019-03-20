package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName

data class XrpServerInfo(
    @SerializedName("load_factor")
    val loadFactor: Long,
    @SerializedName("validated_ledger")
    val validatedLedger: XrpValidatedLedger
)