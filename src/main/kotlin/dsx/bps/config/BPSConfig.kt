package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

object BPSConfig: ConfigSpec("BPS") {
    val threadsForInvoiceObserver by required<Int>()
}