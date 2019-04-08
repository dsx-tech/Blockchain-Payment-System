package dsx.bps.crypto.trx.datamodel

import com.google.gson.annotations.SerializedName

data class TrxBlock(
    @SerializedName("blockID")
    val hash: String,
    @SerializedName("block_header")
    val blockHeader: TrxBlockHeader,
    @SerializedName("transactions")
    private val _transactions: List<TrxTx>?
) {
    val transactions: List<TrxTx>
        get() = _transactions ?: listOf()
}
