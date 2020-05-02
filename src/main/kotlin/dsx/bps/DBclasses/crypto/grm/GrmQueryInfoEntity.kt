package dsx.bps.DBclasses.crypto.grm

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class GrmQueryInfoEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GrmQueryInfoEntity>(GrmQueryInfoTable)

    var queryId by GrmQueryInfoTable.queryId
    var validUntil by GrmQueryInfoTable.validUntil
    var bodyHash by GrmQueryInfoTable.bodyHash
}