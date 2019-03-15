package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName

data class XrpAccountTxs(
    val account: String,
    @SerializedName("ledger_index_max")
    val ledgerIndexMax: Int,
    @SerializedName("ledger_index_min")
    val ledgerIndexMin: Int,
    val transactions: List<XrpAccountTx>
)