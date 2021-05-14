package com.example.pocketrocket.managers

import com.example.pocketrocket.components.CidType
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.systems.GameSystem
import java.util.*
import kotlin.NoSuchElementException

class EntityManager() {
    private var nextID: Long = 0L

    private val entitySignatures = mutableMapOf<EidType, BitSet>()

    fun createEntity(): EidType {
        entitySignatures[nextID] = BitSet()
        return nextID++
    }

    fun destroyEntity(id: EidType) {
        entitySignatures.remove(id)
    }

    fun setSignature(eid: EidType, signature: BitSet): BitSet {
        entitySignatures[eid] = signature
        return signature
    }

    fun setSignature(eid: EidType, cid: CidType, value: Boolean): BitSet {
        return entitySignatures[eid]?.also { it.set(cid, value) }
            ?: throw NoSuchElementException("Entity $eid is not of this world")
    }

    fun getSignature(eid: EidType): BitSet {
        return entitySignatures[eid]
            ?: throw NoSuchElementException("Entity $eid is not of this world")
    }

    fun registerEntitiesToSystem(system: GameSystem) {
        system.clearEntities()
        for ((eid, signature) in entitySignatures.entries) {
            if (system.appliesToSignature(signature))
                system.addEntity(eid)
        }
    }
}