package dsx.bps.DBclasses

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column

object PayableTable: IntIdTable() {
    val type: Column<String> = varchar("type", 10)
}