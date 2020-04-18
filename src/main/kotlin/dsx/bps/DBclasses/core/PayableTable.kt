package dsx.bps.DBclasses.core

import dsx.bps.core.datamodel.PayableType
import org.jetbrains.exposed.dao.IntIdTable

object PayableTable: IntIdTable() {
    val type = enumeration("type", PayableType::class)
}