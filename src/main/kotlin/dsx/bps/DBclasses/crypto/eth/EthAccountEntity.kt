package dsx.bps.DBclasses.crypto.eth

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class EthAccountEntity (id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<EthAccountEntity>(EthAccountTable)
    var address by EthAccountTable.address
    var password by EthAccountTable.password
    var wallet by EthAccountTable.wallet
    var contractAddress by EthAccountTable.contractAddress
}