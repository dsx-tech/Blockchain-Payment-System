package dsx.bps.core.datamodel

enum class TxStatus {
    VALIDATING,
    CONFIRMED,
    // use INCORRECT type if Tx contain not expected data
    INCORRECT,
    REJECTED
}