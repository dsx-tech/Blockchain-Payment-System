package dsx.bps.crypto.grm.datamodel

import dsx.bps.exception.crypto.grm.GrmBocException
import kotlin.math.ceil

class BagOfCells {
    val rootCells: Array<Cell>

    constructor(roots: Array<Cell>) {
        rootCells = roots
    }

    companion object {
        private const val magicGeneric: String = "B5EE9C72"

        fun deserialize(buffer: ByteArray): BagOfCells {
            val magic: String = byteArrayToHex(buffer.sliceArray(0 until 4))
            if (magic == magicGeneric) {
                val byte: Byte = buffer[4]
                val hasIdx: Boolean = byte.toInt().and(128) != 0 //has_idx
                val hasCRC32C: Boolean = byte.toInt().and(64) != 0 //has_crc32c
                val hasCacheBits: Boolean = byte.toInt().and(32) != 0 //has_cache_bits
                val flags: Int = byte.toInt().and(24) //flags {flags = 0}
                val size: Int = byte.toInt().and(7) //size {size <= 4}
                val offBytes: Int = buffer[5].toInt() //off_bytes {off_bytes <= 8}
                var pointer = 6

                val cellsCountInBytes: ByteArray = buffer
                    .sliceArray(pointer until (pointer + size))
                //cells { cells <= 32}, (size * 8) <= 32
                val cellsCount: Long = byteArrayLEtoLong(cellsCountInBytes)
                pointer += size

                if (cellsCount > Int.MAX_VALUE) {
                    throw GrmBocException(
                        "cellsCount $cellsCount more than Int.MAX_VALUE." +
                                " cellsCount in bytes $cellsCountInBytes." +
                                " Boc in hex: " + byteArrayToHex(buffer)
                    )
                }

                val rootsCountInBytes: ByteArray = buffer
                    .sliceArray(pointer until (pointer + size))
                // roots { 1 <= roots <= 32}, (size * 8) <= 32
                val rootsCount: Long = byteArrayLEtoLong(rootsCountInBytes)
                pointer += size

                if (rootsCount > Int.MAX_VALUE) {
                    throw GrmBocException(
                        "rootsCount $rootsCount more than Int.MAX_VALUE." +
                                " rootsCount in bytes $rootsCountInBytes." +
                                " Boc in hex: " + byteArrayToHex(buffer)
                    )
                }

                val absentInBytes: ByteArray = buffer
                    .sliceArray(pointer until (pointer + size))
                // absent { roots + absent <= cells}, (size * 8) <= 32
                val absent: Long = byteArrayLEtoLong(absentInBytes)
                pointer += size

                if (absent > Int.MAX_VALUE) {
                    throw GrmBocException(
                        "absent $absent more than Int.MAX_VALUE." +
                                " absent in bytes $absentInBytes." +
                                " Boc in hex: " + byteArrayToHex(buffer)
                    )
                }

                val totCellsSizeInByte: ByteArray = buffer
                    .sliceArray(pointer until (pointer + offBytes))
                // tot_cell_size { tot_cell_size <= 64 }, (off_bytes * 8) <= 64
                val totCellsSize: Long = byteArrayBEtoLong(totCellsSizeInByte)
                pointer += offBytes

                if (totCellsSize > Int.MAX_VALUE) {
                    throw GrmBocException(
                        "totCellsSize $totCellsSize more than Int.MAX_VALUE." +
                                " totCellsSize in bytes $totCellsSizeInByte." +
                                " Boc in hex: " + byteArrayToHex(buffer)
                    )
                }


                val rootList = IntArray(rootsCount.toInt()) // root_list
                for (i in rootList.indices) {
                    val rootNumInBytes: ByteArray = buffer
                        .sliceArray(pointer until (pointer + size))
                    pointer += size
                    rootList[i] = byteArrayLEtoLong(rootNumInBytes).toInt()
                }

                var indexes: IntArray? = null
                if (hasIdx) {
                    indexes = IntArray(cellsCount.toInt())
                    for (i in indexes.indices) {
                        val indexNumInBytes: ByteArray = buffer
                            .sliceArray(pointer until (pointer + offBytes))
                        pointer += offBytes
                        indexes[i] = byteArrayLEtoLong(indexNumInBytes).toInt()
                    }
                }

                if (totCellsSize > Int.MAX_VALUE - 8 - pointer) {
                    throw IllegalArgumentException("")
                }

                var cellData: ByteArray = buffer
                    .sliceArray(pointer until (pointer + totCellsSize.toInt()))
                pointer += totCellsSize.toInt()

                if (hasCRC32C) {
                    val checkSum: ByteArray = buffer
                        .sliceArray((buffer.size - 4) until buffer.size)
                    //TODO: check crc32c
                    // crc32c implement in java 9 standard library
                }

                val cells: ArrayList<Cell> = arrayListOf()
                val refs: Array<ArrayList<Long>> = Array(cellsCount.toInt()) { arrayListOf<Long>() }
                for (i in 0 until cellsCount.toInt()) {
                    val d1: Byte = cellData[0]
                    val refsCount: Int = d1.toInt() and 7
                    val isSpecial: Boolean = d1.toInt() and 8 != 0
                    val withHashes: Boolean = d1.toInt() and 16 != 0
                    val levelMask = d1.toInt() shr 5

                    val d2InByte: Byte = cellData[1]
                    val d2: Int = byteArrayLEtoLong(
                        arrayOf<Byte>(d2InByte)
                            .toByteArray()
                    ).toInt()
                    val cellSize: Float = d2.toFloat() / 2
                    val cellFSize: Int = ceil(cellSize).toInt()

                    val cellBuffer: ByteArray = cellData
                        .sliceArray(2 until (2 + cellFSize))

                    cells.add(
                        Cell(
                            isSpecial, indexes?.get(i) ?: 0,
                            (cellSize * 8).toInt(), cellBuffer, arrayListOf<Cell>()
                        )
                    )

                    val refsBuffer: ByteArray = cellData
                        .sliceArray((2 + cellFSize) until (2 + cellFSize + refsCount * size))
                    for (r in 0 until refsCount) {
                        val refInBytes: ByteArray = refsBuffer
                            .sliceArray(r * size until (r + 1) * size)
                        val ref: Long = byteArrayBEtoLong(refInBytes)
                        refs[i].add(ref)
                    }

                    cellData = cellData
                        .sliceArray(
                            (2 + cellFSize + refsBuffer.size)
                                    until cellData.size
                        )
                }

                for (i in 0 until cellsCount.toInt()) {
                    for (j in 0 until refs[i].size) {
                        cells[i].putRef(cells[refs[i][j].toInt()])
                    }
                }

                val rootCells: ArrayList<Cell> = arrayListOf()
                for (i in rootList) {
                    rootCells.add(cells[i])
                }
                return BagOfCells(rootCells.toTypedArray())

            } else {
                throw GrmBocException(
                    "The magic $magic is not equal to $magicGeneric." +
                            " This type of boc is not supported."
                )
            }
        }

    }
}