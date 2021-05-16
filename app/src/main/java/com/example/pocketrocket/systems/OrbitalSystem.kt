package com.example.pocketrocket.systems

import com.example.pocketrocket.components.OrbitComponent
import com.example.pocketrocket.components.ParentComponent
import com.example.pocketrocket.components.PositionComponent
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.managers.ECSCallback
import com.example.pocketrocket.utils.Vec2D
import java.util.*

class OrbitalSystem(callback: ECSCallback) : GameSystem(callback) {

    fun updateOrbits(t: Float) {
        for (eid in entityList) {
            val position = callback.getComponent<PositionComponent>(eid, PositionComponent.componentID)
            val orbit = callback.getComponent<OrbitComponent>(eid, OrbitComponent.componentID)

            val dt = t - orbit.t0
            position.pos = Vec2D.createVecPolar(orbit.r0 + orbit.vr * dt, orbit.va * dt)

            if (callback.hasComponent(eid, ParentComponent.componentID)) {
                val parent = callback.getComponent<ParentComponent>(eid, ParentComponent.componentID)
                val parentPosition = callback.getComponent<PositionComponent>(parent.parentEid, PositionComponent.componentID)
                position.pos += parentPosition.pos
            }
        }
    }

    fun deOrbit(rLimit2: Float) {
        val toBeDestroyed = mutableListOf<EidType>()
        for (eid in entityList.toList()) {
            val position = callback.getComponent<PositionComponent>(eid, PositionComponent.componentID)
            if (position.pos.r2 > rLimit2)
            // Cannot call destroyEntity here since it would modify the list we are iterating over
                toBeDestroyed.add(eid)
        }
        for (eid in toBeDestroyed)
            callback.destroyEntity(eid)
    }

    override fun appliesToSignature(signature: BitSet): Boolean {
        if (!signature.get(PositionComponent.componentID)) return false
        if (!signature.get(OrbitComponent.componentID)) return false
        return true
    }
}