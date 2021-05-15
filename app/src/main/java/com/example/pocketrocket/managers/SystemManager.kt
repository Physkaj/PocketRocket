package com.example.pocketrocket.managers

import com.example.pocketrocket.components.CidType
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.systems.GameSystem
import java.util.*

class SystemManager() {
    private var systems = Vector<GameSystem>()

    fun addSystem(system: GameSystem) {
        systems.add(system)
    }

    fun removeSystem(system: GameSystem) {
        systems.remove(system)
    }

    fun componentAdded(eid: EidType, signature: BitSet) {
        for (system in systems.filter { !it.hasEntity(eid) }) {
            if (system.appliesToSignature(signature))
                system.addEntity(eid)
        }
    }

    fun componentRemoved(eid: EidType, signature: BitSet) {
        for (system in systems.filter { it.hasEntity(eid) }) {
            if (!system.appliesToSignature(signature))
                system.removeEntity(eid)
        }
    }

    fun entityDestroyed(eid: EidType) {
        for (system in systems)
            if (system.hasEntity(eid))
                system.removeEntity(eid)
    }
}