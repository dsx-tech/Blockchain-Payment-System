package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

object PaymentProcessorConfig: ConfigSpec("paymentProcessor") {
    val frequency by required<Long>()
}
