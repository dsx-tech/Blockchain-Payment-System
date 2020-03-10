package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import dsx.bps.core.datamodel.PayableType

object PayableTable: IntIdTable() {
    val type = enumeration("type", PayableType::class)
}