package com.example.pocketrocket.systems

import com.example.pocketrocket.components.GravityComponent
import com.example.pocketrocket.components.PositionComponent
import com.example.pocketrocket.components.PhysicalBodyComponent
import com.example.pocketrocket.managers.ECSCallback
import com.example.pocketrocket.utils.Vec2D
import java.util.*
import kotlin.math.sqrt

/*
 * Gravity constant is set to 1, masses are given in arbitrary units
 */
class GravitySystem(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(GravityComponent.componentID) &&
                signature.get(PhysicalBodyComponent.componentID)
    }

    fun gravitate(dt: Float) {
        for (i in 0 until entityList.size) {
            val eid1 = entityList.elementAt(i)
            val position1 = callback.getComponent<PositionComponent>(eid1, PositionComponent.componentID)
            val gravity1 = callback.getComponent<GravityComponent>(eid1, GravityComponent.componentID)
            val physical1 = callback.getComponent<PhysicalBodyComponent>(eid1, PhysicalBodyComponent.componentID)
            for (j in i + 1 until entityList.size) {
                val eid2 = entityList.elementAt(j)
                val position2 = callback.getComponent<PositionComponent>(eid2, PositionComponent.componentID)
                val gravity2 = callback.getComponent<GravityComponent>(eid2, GravityComponent.componentID)
                val physical2 = callback.getComponent<PhysicalBodyComponent>(eid2, PhysicalBodyComponent.componentID)

                val dp = Vec2D(position1.x - position2.x, position1.y - position2.y)
                val r = dp.r
                val r3 = r * r * r

                physical1.acc = -dp * gravity2.mass / r3
                physical2.acc = dp * gravity1.mass / r3
            }
        }
    }
}