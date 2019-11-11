package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

class PaymentProcessorConfig {
    companion object: ConfigSpec("paymentProcessorConfig"){
        val frequency by required<Long>()
    }
}