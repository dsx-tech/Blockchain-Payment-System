package dsx.bps.crypto.eth.datamodel

import java.math.BigDecimal

/**
 * @param address is address of mined contract
 * @param fee is deploying fee in Ether
 */
data class SmartContract(
    val address: String,
    val fee: BigDecimal
)