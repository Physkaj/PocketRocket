package com.example.pocketrocket.systems

import com.example.pocketrocket.components.GravityComponent
import com.example.pocketrocket.components.OrbitComponent
import com.example.pocketrocket.components.ParentComponent
import com.example.pocketrocket.components.PositionComponent
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.managers.ECSCallback
import com.example.pocketrocket.utils.Vec3D
import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt

class OrbitalMotionSystem(callback: ECSCallback) : GameSystem(callback) {

    fun updateOrbits(dt: Float) {
        for (eid in entityList) {
            val position = callback.getComponent<PositionComponent>(eid, PositionComponent.componentID)
            val orbit = callback.getComponent<OrbitComponent>(eid, OrbitComponent.componentID)
            val parent = callback.getComponent<ParentComponent>(eid, ParentComponent.componentID)
            val parentPosition = callback.getComponentOrNull<PositionComponent>(parent.parentEid, PositionComponent.componentID)
                ?: throw(NoSuchElementException("Orbit parent is missing a position component"))
            val parentGravity = callback.getComponentOrNull<GravityComponent>(parent.parentEid, GravityComponent.componentID)
            val parentMass = parentGravity?.mass ?: 1f

            val r2 = (position.pos - parentPosition.pos).r2
            val dArg = orbit.b / r2 * sqrt(parentMass / orbit.a) * dt
            orbit.arg += dArg

            var vec = Vec3D(orbit.a * cos(orbit.arg) + orbit.c, orbit.b * sin(orbit.arg), 0f)
            vec.rotateZ(orbit.argApoapsis)
            vec.rotateY(orbit.inclination)

            // Set position and translate to whatever it is orbiting
            position.pos.x = vec.x + parentPosition.pos.x
            position.pos.y = vec.y + parentPosition.pos.y
            position.pos.z = vec.z + parentPosition.pos.z
        }
    }

    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(PositionComponent.componentID) &&
                signature.get(OrbitComponent.componentID) &&
                signature.get(ParentComponent.componentID)
    }
}