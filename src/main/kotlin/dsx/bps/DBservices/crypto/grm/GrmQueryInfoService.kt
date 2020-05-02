package dsx.bps.DBservices.crypto.grm

import dsx.bps.DBclasses.crypto.grm.GrmQueryInfoEntity
import dsx.bps.DBclasses.crypto.grm.GrmQueryInfoTable
import dsx.bps.DBservices.Datasource
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

class GrmQueryInfoService(datasource: Datasource) {

    init {
        transaction(datasource.getConnection()) {
            if (!GrmQueryInfoTable.exists())
                SchemaUtils.create(GrmQueryInfoTable)
        }
    }

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