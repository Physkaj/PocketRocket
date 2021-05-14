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

    private fun calcSpawn(t0: Float, spawnProbabilityPerSec: Float): Float = t0 - ln(1f - Random.nextFloat()) / spawnProbabilityPerSec

    fun activate(t: Float) {
        for (eid in entityList) {
            val spawner = callback.getComponent<StarSpawnComponent>(eid, StarSpawnComponent.componentID)
            if (t >= spawner.nextSpawn) {
                createStar(t, eid)
                spawner.nextSpawn = calcSpawn(t, spawner.spawnProbabilityPerSec)
            }
        }

    }

    private fun createStar(creationTime: Float, parentEid: EidType) {
        val eid = callback.createEntity()
        val parent = callback.addComponent<ParentComponent>(eid, ParentComponent.componentID)
        val position = callback.addComponent<PositionComponent>(eid, PositionComponent.componentID)
        val orbital = callback.addComponent<OrbitComponent>(eid, OrbitComponent.componentID)
        val shape = callback.addComponent<ShapeComponent>(eid, ShapeComponent.componentID)
        val color = callback.addComponent<ColorComponent>(eid, ColorComponent.componentID)
        parent.parentEid = parentEid
        orbital.t0 = creationTime
        orbital.va = 0.01f
        orbital.vr = 0.1f
        shape.shapeType = ShapeComponent.ShapeType.CIRCLE
        shape.r = 0.01f
        color.color = Color.rgb(255, 255, 255) /*White*/
    }
}