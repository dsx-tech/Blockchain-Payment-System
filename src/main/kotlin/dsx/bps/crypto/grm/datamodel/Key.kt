package dsx.bps.crypto.grm.datamodel

import drinkless.org.ton.TonApi

data class Key(
    val publicKey: String,
    val secret: ByteArray
) {
    constructor(key: TonApi.Key) : this(
        key.publicKey, key.secret
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Key

        if (publicKey != other.publicKey) return false
        if (!secret.contentEquals(other.secret)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = publicKey.hashCode()
        result = 31 * result + secret.contentHashCode()
        return result
    }
}