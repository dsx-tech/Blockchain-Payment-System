package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

object DatabaseConfig: ConfigSpec("database") {
    val connectionURL by required<String>()
    val driver by required<String>()
}