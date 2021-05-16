package com.example.pocketrocket.systems

import com.example.pocketrocket.components.PhysicalBodyComponent
import com.example.pocketrocket.managers.ECSCallback
import java.util.*

class ResetAccelerationSystem(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(PhysicalBodyComponent.componentID)
    }

    fun reset() {
        for (eid in entityList) {
            val physical = callback.getComponent<PhysicalBodyComponent>(eid, PhysicalBodyComponent.componentID)
            physical.acc.clear()
        }
    }
}