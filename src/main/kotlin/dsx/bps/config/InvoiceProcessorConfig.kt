package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

object InvoiceProcessorConfig: ConfigSpec("invoiceProcessorConfig") {
    val frequency by required<Long>()
}
