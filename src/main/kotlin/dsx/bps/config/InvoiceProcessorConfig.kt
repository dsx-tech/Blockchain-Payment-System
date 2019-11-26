package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

object InvoiceProcessorConfig: ConfigSpec("invoiceProcessor") {
    val frequency by required<Long>()
}
