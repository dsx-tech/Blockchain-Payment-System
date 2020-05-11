package dsx.bps.DBclasses.crypto.erc20

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Erc20ContractEntity(id: EntityID<Int>): IntEntity(id) {
        companion object: IntEntityClass<Erc20ContractEntity>(Erc20ContractTable)
        var contractAddress by Erc20ContractTable.contractAddress
        var decimals by Erc20ContractTable.decimals
        var owner by Erc20ContractTable.owner
}