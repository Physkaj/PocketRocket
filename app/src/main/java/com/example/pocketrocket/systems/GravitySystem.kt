package com.example.pocketrocket.systems

import com.example.pocketrocket.components.GravityComponent
import com.example.pocketrocket.components.PositionComponent
import com.example.pocketrocket.components.PhysicalBodyComponent
import com.example.pocketrocket.managers.ECSCallback
import com.example.pocketrocket.utils.Vec3D
import java.util.*

/*
 * Gravity constant is set to 1, masses are given in arbitrary units
 */
class GravitySystem(callback: ECSCallback, var significantMassLimit: Float = 0f) : GameSystem(callback) {
    companion object {
        fun calculateForce(p1: Vec3D, m1: Float, p2: Vec3D, m2: Float): Vec3D {
            val dx = p2 - p1
            val r = dx.r
            val r3 = r * r * r

            return dx * m1 * m2 / r3
        }
    }

    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(GravityComponent.componentID) &&
                signature.get(PositionComponent.componentID)
    }

    fun gravitate() {
        for (i in 0 until entityList.size) {
            val eid1 = entityList.elementAt(i)
            val gravity1 = callback.getComponent<GravityComponent>(eid1, GravityComponent.componentID)
            // Reduce the number of calculations by only ignoring interactions between small masses
            if (gravity1.mass < significantMassLimit) continue
            val position1 = callback.getComponent<PositionComponent>(eid1, PositionComponent.componentID)
            val physical1 = callback.getComponentOrNull<PhysicalBodyComponent>(eid1, PhysicalBodyComponent.componentID)
            for (j in i + 1 until entityList.size) {
                val eid2 = entityList.elementAt(j)
                val position2 = callback.getComponent<PositionComponent>(eid2, PositionComponent.componentID)
                val gravity2 = callback.getComponent<GravityComponent>(eid2, GravityComponent.componentID)
                val physical2 = callback.getComponentOrNull<PhysicalBodyComponent>(eid2, PhysicalBodyComponent.componentID)

                // Force on object 1
                val force = calculateForce(position1.pos, gravity1.mass, position2.pos, gravity2.mass)

                // Allow for immovable gravity objects
                physical1?.acc?.addTo(force / gravity1.mass)
                physical2?.acc?.subFrom(force / gravity2.mass)
            }
        }
    }
}