package dsx.bps.crypto.grm.datamodel

import dsx.bps.exception.crypto.grm.GrmBocException

class Cell(
    val special: Boolean,
    val index: Int,//in bits, max 128*8 bit
    val length: Int,// max 128
    val data: ByteArray,
    val references: ArrayList<Cell>
) {
    fun putRef(ref: Cell) {
        if (this.references.size > 4) {
            throw GrmBocException("Number of references to the other cells should not be more than four.")
        }
        this.references.add(ref)
    }
}