package dsx.bps.config

import com.uchuhimo.konf.ConfigSpec

object DepositAccountProcessorConfig: ConfigSpec("depositAccountProcessor") {
    val frequency by required<Long>()
}