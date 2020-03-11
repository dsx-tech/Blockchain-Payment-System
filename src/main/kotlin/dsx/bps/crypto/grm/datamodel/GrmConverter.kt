package dsx.bps.crypto.grm.datamodel

fun byteArrayToHex(data: ByteArray): String {
    var hexString = ""
    for (byte in data) {
        val firstChar = (byte.toInt().shr(4) and (0xF)).toString(16)
        val secondChar = (byte.toInt() and (0xF)).toString(16)
        hexString = hexString + firstChar + secondChar
    }
    return hexString.toUpperCase()
}

fun hexToByteArray(hex: String): ByteArray {
    if (hex.length % 2 == 1) {
        throw IllegalArgumentException(
            "Invalid hexadecimal String supplied."
        )
    }

    val byteArrayList = ArrayList<Byte>()
    for (i in hex.indices step 2) {
        val substr = hex.substring(i, i + 2)
        val firstDigit = substr[0].toString().toInt(16)
        val secondDigit = substr[1].toString().toInt(16)
        val resultByte = ((firstDigit.shl(4)) + secondDigit).toByte()
        byteArrayList.add(resultByte)
    }

    return byteArrayList.toByteArray()
}