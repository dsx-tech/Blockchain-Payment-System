package dsx.bps.crypto.grm.datamodel

import dsx.bps.exception.crypto.grm.GrmBocException
import java.util.*
import kotlin.collections.ArrayList

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

//Convert array of 4 bytes in little-endian to Long
fun byteArrayLEtoLong(buffer: ByteArray): Long {
    if (buffer.size > 5) {
        throw IllegalArgumentException("Byte array size ${buffer.size} more than 4")
    } else {
        var value: Long = 0
        for (i in buffer.indices) {
            value += (buffer[i].toLong() and 0xffL) shl (8 * i)
        }
        return value
    }
}

//Convert array of 4 bytes in big-endian to Long
fun byteArrayBEtoLong(buffer: ByteArray): Long {
    if (buffer.size > 5) {
        throw IllegalArgumentException("Byte array size ${buffer.size} more than 4")
    } else {
        var value: Long = 0
        for (i in buffer.indices) {
            value = (value shl 8) + (buffer[i].toLong() and 0xffL)
        }
        return value
    }
}

fun bocDataToText(byteArray: ByteArray): String {
    val rootBagOfCells: BagOfCells = BagOfCells.deserialize(byteArray)
    val strInBytes: ArrayList<Byte> = arrayListOf()
    val arrayDeque: ArrayDeque<BagOfCells> = ArrayDeque()
    arrayDeque.addFirst(rootBagOfCells)

    while (arrayDeque.isNotEmpty()) {
        val bagOfCells: BagOfCells = arrayDeque.pop()
        if (bagOfCells.length % 8 != 0) {
            throw GrmBocException(
                "Could not parse to UTF-8 string from data "
                        + byteArrayToHex(bagOfCells.data) + ".\n"
                        + "Length data is ${bagOfCells.length / 8} bytes ${bagOfCells.length % 8} bits"
            )
        }
        strInBytes.addAll(bagOfCells.data.toTypedArray())
        for (ref in bagOfCells.references) {
            arrayDeque.addLast(ref)
        }
    }

    return strInBytes.toByteArray().toString(Charsets.UTF_8)
}