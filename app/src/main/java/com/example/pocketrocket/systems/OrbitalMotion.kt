package com.example.pocketrocket.systems

import com.example.pocketrocket.components.OrbitComponent
import com.example.pocketrocket.components.ParentComponent
import com.example.pocketrocket.components.PositionComponent
import com.example.pocketrocket.managers.ECSCallback
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class OrbitalMotion(callback: ECSCallback) : GameSystem(callback) {

    fun activate(t: Float) {
        for (eid in entityList) {
            val position = callback.getComponent<PositionComponent>(eid, PositionComponent.componentID)
            val orbit = callback.getComponent<OrbitComponent>(eid, OrbitComponent.componentID)

            position.x = (orbit.r0 + orbit.vr) * cos(orbit.va * (t - orbit.t0))
            position.y = (orbit.r0 + orbit.vr) * sin(orbit.va * (t - orbit.t0))

            if (callback.hasComponent(eid, ParentComponent.componentID)) {
                val parent = callback.getComponent<ParentComponent>(eid, ParentComponent.componentID)
                val parentPosition = callback.getComponent<PositionComponent>(parent.parentEid, PositionComponent.componentID)
                position.x += parentPosition.x
                position.y += parentPosition.y
            }
        }
    }

    override fun appliesToSignature(signature: BitSet): Boolean {
        if (!signature.get(PositionComponent.componentID)) return false
        if (!signature.get(OrbitComponent.componentID)) return false
        return true
    }
}