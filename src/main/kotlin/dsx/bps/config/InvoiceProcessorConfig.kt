package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

class InvoiceProcessorConfig {
    companion object: ConfigSpec("invoiceProcessorConfig"){
        val frequency by required<Long>()
    }
}