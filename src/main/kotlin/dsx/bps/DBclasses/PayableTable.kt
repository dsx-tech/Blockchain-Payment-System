package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import dsx.bps.core.datamodel.Type

object PayableTable: IntIdTable() {
    val type = enumeration("type", Type::class)
}