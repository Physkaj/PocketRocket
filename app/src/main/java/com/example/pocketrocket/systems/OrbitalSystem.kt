package com.example.pocketrocket.systems

import com.example.pocketrocket.components.OrbitComponent
import com.example.pocketrocket.components.ParentComponent
import com.example.pocketrocket.components.PositionComponent
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.managers.ECSCallback
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class OrbitalSystem(callback: ECSCallback) : GameSystem(callback) {

    fun updateOrbits(t: Float) {
        for (eid in entityList) {
            val position = callback.getComponent<PositionComponent>(eid, PositionComponent.componentID)
            val orbit = callback.getComponent<OrbitComponent>(eid, OrbitComponent.componentID)

            val dt = t - orbit.t0
            position.x = (orbit.r0 + orbit.vr * dt) * cos(orbit.va * dt)
            position.y = (orbit.r0 + orbit.vr * dt) * sin(orbit.va * dt)

            if (callback.hasComponent(eid, ParentComponent.componentID)) {
                val parent = callback.getComponent<ParentComponent>(eid, ParentComponent.componentID)
                val parentPosition = callback.getComponent<PositionComponent>(parent.parentEid, PositionComponent.componentID)
                position.x += parentPosition.x
                position.y += parentPosition.y
            }
        }
    }

    fun deOrbit(rLimit: Float = 2f) {
        val rLimit2 = rLimit * rLimit

        val toBeDestroyed = mutableListOf<EidType>()
        for (eid in entityList.toList()) {
            val position = callback.getComponent<PositionComponent>(eid, PositionComponent.componentID)
            if (position.x * position.x + position.y * position.y > rLimit2)
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