package dsx.bps.crypto.xrp.datamodel

import com.google.gson.annotations.SerializedName

data class XrpLedger(
    val closed: Boolean,
    val seqNum: String,
    val transactions: List<XrpTx>? = null,
    @SerializedName("ledger_hash")
    val hash: String,
    @SerializedName("ledger_index")
    val index: Long,
    @SerializedName("parent_hash")
    val previousHash: String
)