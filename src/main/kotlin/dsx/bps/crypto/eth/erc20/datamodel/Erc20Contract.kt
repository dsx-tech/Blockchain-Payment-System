package dsx.bps.crypto.eth.erc20.datamodel

data class Erc20Contract(
    val address: String,
    val decimals: Int,
    val owner: String
)