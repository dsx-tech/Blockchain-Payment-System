package dsx.bps.DBservices.crypto.grm

import dsx.bps.DBclasses.crypto.grm.GrmQueryInfoEntity
import dsx.bps.DBclasses.crypto.grm.GrmQueryInfoTable
import org.jetbrains.exposed.sql.transactions.transaction

class GrmQueryInfoService {

    fun add(queryId: Long, validUntil: Long, bodyHash: String): GrmQueryInfoEntity {
        return transaction {
            GrmQueryInfoEntity.new {
                this.queryId = queryId
                this.validUntil = validUntil
                this.bodyHash = bodyHash
            }
        }
    }

    fun findByHash(hash: String): GrmQueryInfoEntity? {
        return transaction {
            GrmQueryInfoEntity.find {
                GrmQueryInfoTable.bodyHash eq hash
            }
        }.singleOrNull()
    }

}