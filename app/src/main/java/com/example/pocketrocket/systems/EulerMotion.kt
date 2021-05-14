package com.example.pocketrocket.systems

import com.example.pocketrocket.components.PositionComponent
import com.example.pocketrocket.components.VelocityComponent
import com.example.pocketrocket.managers.ECSCallback
import java.util.*

class EulerMotion(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(PositionComponent.componentID) &&
                signature.get(VelocityComponent.componentID)
    }

    fun activate(dt: Float) {
        for (eid in entityList) {
            val position = callback.getComponent<PositionComponent>(eid, PositionComponent.componentID)
            val velocity = callback.getComponent<VelocityComponent>(eid, VelocityComponent.componentID)

            position.x += velocity.vx * dt
            position.y += velocity.vy * dt
        }
    }
}