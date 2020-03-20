package dsx.bps.crypto.grm

import dsx.bps.crypto.grm.datamodel.bocDataToText
import dsx.bps.crypto.grm.datamodel.hexToByteArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GrmBocUnitTest {

    @Test
    @DisplayName("Boc of one cell deserialize test")
    fun bocDeserializeCell() {
        // абвгдеёжзийклмнопрстуфхцчшщъыьэюя in boc
        val hex =
            "B5EE9C72410101010044000084D0B0D0B1D0B2D0B3D0B4D0B5D191D0B6D0B7D0B8D0B9D0BAD0BBD0BCD0BDD0BED0BFD180D181D182D183D184D185D186D187D188D189D18AD18BD18CD18DD18ED18F82B68295"
        val result: String = bocDataToText(hexToByteArray(hex))
        Assertions.assertEquals(result, "абвгдеёжзийклмнопрстуфхцчшщъыьэюя")
    }

    @Test
    @DisplayName("Boc of more than one cell deserialize test")
    fun bocDeserializeCells() {
        // 1абвгдеёжзийклмнопрстуфхцчшщъыьэюя12абвгдеёжзийклмнопрстуфхцчшщъыьэюя23абвгдеёжзийклмнопрстуфхцчшщъыьэюя3 in boc
        val hex =
            "B5EE9C724101020100D10001FE31D0B0D0B1D0B2D0B3D0B4D0B5D191D0B6D0B7D0B8D0B9D0BAD0BBD0BCD0BDD0BED0BFD180D181D182D183D184D185D186D187D188D189D18AD18BD18CD18DD18ED18F3132D0B0D0B1D0B2D0B3D0B4D0B5D191D0B6D0B7D0B8D0B9D0BAD0BBD0BCD0BDD0BED0BFD180D181D182D183D184D185D186D187D188D189D18AD18B01009AD18CD18DD18ED18F3233D0B0D0B1D0B2D0B3D0B4D0B5D191D0B6D0B7D0B8D0B9D0BAD0BBD0BCD0BDD0BED0BFD180D181D182D183D184D185D186D187D188D189D18AD18BD18CD18DD18ED18F33D9F93235"
        val result: String = bocDataToText(hexToByteArray(hex))
        Assertions.assertEquals(
            result, "1абвгдеёжзийклмнопрстуфхцчшщъыьэюя1" +
                    "2абвгдеёжзийклмнопрстуфхцчшщъыьэюя23абвгдеёжзийклмнопрстуфхцчшщъыьэюя3"
        )
    }

}