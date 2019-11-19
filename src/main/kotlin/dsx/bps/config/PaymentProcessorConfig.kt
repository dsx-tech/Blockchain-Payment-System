package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

object PaymentProcessorConfig: ConfigSpec("paymentProcessorConfig") {
    val frequency by required<Long>()
}
