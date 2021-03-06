package com.example.pocketrocket.systems

import com.example.pocketrocket.components.PositionComponent
import com.example.pocketrocket.components.PhysicalBodyComponent
import com.example.pocketrocket.managers.ECSCallback
import java.util.*

class EulerMotionSystem(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(PositionComponent.componentID) &&
                signature.get(PhysicalBodyComponent.componentID)
    }

    fun activate(dt: Float) {
        for (eid in entityList) {
            val position = callback.getComponent<PositionComponent>(eid, PositionComponent.componentID)
            val physical = callback.getComponent<PhysicalBodyComponent>(eid, PhysicalBodyComponent.componentID)

            position.pos += physical.vel * dt
            physical.vel += physical.acc * dt
            position.pos += physical.acc * dt * dt * 0.5f
        }
    }
}