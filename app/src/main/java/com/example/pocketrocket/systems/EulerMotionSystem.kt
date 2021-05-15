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

            // TODO: 2021-05-15 Replace use of PositionComponent
            position.x += physical.vel.x * dt
            position.y += physical.vel.y * dt
            physical.vel += physical.acc * dt
            position.x += physical.acc.x * physical.acc.x * dt * 0.5f
            position.y += physical.acc.y * physical.acc.y * dt * 0.5f
        }
    }
}