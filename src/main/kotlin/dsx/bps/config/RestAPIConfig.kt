package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

object RestAPIConfig : ConfigSpec("RESTAPI") {
    val host by RestAPIConfig.required<String>()
    val port by RestAPIConfig.required<Int>()
}