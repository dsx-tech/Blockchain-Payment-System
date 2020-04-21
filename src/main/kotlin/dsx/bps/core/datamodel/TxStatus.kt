package dsx.bps.core.datamodel

enum class TxStatus {
    VALIDATING,
    CONFIRMED,

    // use WRONG type if Tx contain not expected data
    INCORRECT,
    REJECTED
}