package com.example.pocketrocket.systems

import android.graphics.Color
import com.example.pocketrocket.components.*
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.managers.ECSCallback
import java.util.*
import kotlin.math.ln
import kotlin.random.Random

class StarSpawningSystem(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        if (!signature.get(StarSpawnComponent.componentID)) return false
        return true
    }

    fun spawn(t: Float) {
        for (eid in entityList) {
            val spawner = callback.getComponent<StarSpawnComponent>(eid, StarSpawnComponent.componentID)
            if (t >= spawner.nextSpawn) {
                createStar(t, eid, spawner.starRadialVelocity, spawner.starAngularVelocity, spawner.starRadius)
                spawner.nextSpawn = t + randomNextSpawn(spawner)
            }
        }

    }

    fun retroactiveStarCreation(t: Float) {
        val currentTime: Float = System.nanoTime() * 1e-9f
        for (eid in entityList) {
            val spawner = callback.getComponent<StarSpawnComponent>(eid, StarSpawnComponent.componentID)
            spawner.nextSpawn = -t
            while (spawner.nextSpawn < 0) {
                spawner.nextSpawn += randomNextSpawn(spawner)
                createStar(currentTime + spawner.nextSpawn, eid, spawner.starRadialVelocity, spawner.starAngularVelocity, spawner.starRadius)
            }
        }
    }

    private fun randomNextSpawn(spawner: StarSpawnComponent) =
        spawner.nextSpawnMinSec + Random.nextFloat() * (spawner.nextSpawnMaxSec - spawner.nextSpawnMinSec)

    private fun createStar(creationTime: Float, parentEid: EidType, radialVelocity: Float, angularVelocity: Float, radius: Float) {
        val eid = callback.createEntity()
        val parent = callback.addComponent<ParentComponent>(eid, ParentComponent.componentID)
        val position = callback.addComponent<PositionComponent>(eid, PositionComponent.componentID)
        val orbital = callback.addComponent<OrbitComponent>(eid, OrbitComponent.componentID)
        val shape = callback.addComponent<ShapeComponent>(eid, ShapeComponent.componentID)
        val color = callback.addComponent<ColorComponent>(eid, ColorComponent.componentID)
        parent.parentEid = parentEid
        orbital.t0 = creationTime
        orbital.va = angularVelocity
        orbital.vr = radialVelocity
        shape.shapeType = ShapeComponent.ShapeType.CIRCLE
        shape.r = radius
        color.color = Color.rgb(255, 255, 255) /*White*/
    }
}