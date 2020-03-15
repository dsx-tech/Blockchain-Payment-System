package dsx.bps.crypto.grm.datamodel

fun convertDataToText(byteArray: ByteArray): String {
    //TODO: Сделать парсер msgData в msgText
    return byteArray.toString(Charsets.UTF_8)
}