package dsx.bps.crypto.eth

import java.math.BigDecimal
import java.math.BigInteger

fun hexToEth(hex: String): BigDecimal {
    return BigInteger(hex.substring(2), 16).toBigDecimal().divide(Math.pow(10.0, 18.0).toBigDecimal(), 15, 1)
}

fun hexToWei(hex: String): BigDecimal {
    return BigInteger(hex.substring(2), 16).toBigDecimal()
}

fun hexToBigInt(hex: String): BigInteger {
    return BigInteger(hex.substring(2), 16)
}

fun bigIntToHex(bInt: BigInteger): String {
    return "0x" + bInt.toString(16)
}

