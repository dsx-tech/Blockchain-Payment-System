package dsx.bps.crypto.grm.datamodel

import dsx.bps.ton.api.TonApi
import java.math.BigDecimal

data class GrmFullAccountState(
    val balance: BigDecimal,
    val lastTxId: GrmInternalTxId,
    val blockIdExt: GrmBlockIdExt,
    val syncUtime: Long
) {
    constructor(fullAccountState: TonApi.FullAccountState) : this(
        BigDecimal(fullAccountState.balance),
        GrmInternalTxId(fullAccountState.lastTransactionId),
        GrmBlockIdExt(fullAccountState.blockId),
        fullAccountState.syncUtime
    )
}

