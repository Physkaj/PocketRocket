package com.example.pocketrocket.managers

import android.graphics.Canvas
import com.example.pocketrocket.components.*
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.systems.GameSystem
import java.util.*
import kotlin.reflect.KClass

interface ECSCallback {
    fun createEntity(): EidType
    fun destroyEntity(eid: EidType)
    fun getSignature(eid: EidType): BitSet
    fun hasComponent(eid: EidType, cid: CidType): Boolean
    fun <T : IGameComponent> getComponent(eid: EidType, cid: CidType): T
    fun <T : IGameComponent> addComponent(eid: EidType, cid: CidType): T
}

abstract class ECSManager : ECSCallback {
    companion object {
        var currentManager: ECSManager? = null
            private set
    }

    private val entityManager = EntityManager()
    private val componentManager = ComponentManager()
    private val systemManager = SystemManager()

    override fun createEntity(): EidType {
        return entityManager.createEntity()
    }

    override fun destroyEntity(eid: EidType) {
        val signature = entityManager.getSignature(eid)
        entityManager.destroyEntity(eid)
        componentManager.entityDestroyed(eid, signature)
        systemManager.entityDestroyed(eid)
    }

    override fun getSignature(eid: EidType): BitSet {
        return entityManager.getSignature(eid)
    }

    override fun hasComponent(eid: EidType, cid: CidType): Boolean {
        return entityManager.getSignature(eid)[cid]
    }

    override fun <T : IGameComponent> getComponent(eid: EidType, cid: CidType): T {
        return componentManager.getComponent<T>(eid, cid)
    }

    protected fun registerComponent(componentClass: KClass<out IGameComponent>) {
        componentManager.registerComponent(componentClass)
    }

    override fun <T : IGameComponent> addComponent(eid: EidType, cid: CidType): T {
        val component = componentManager.addComponent<T>(eid, cid)
        val signature = entityManager.setSignature(eid, cid, true)
        systemManager.componentAdded(eid, signature)
        return component
    }

    protected fun removeComponent(eid: EidType, cid: CidType) {
        componentManager.removeComponent(eid, cid)
        val signature = entityManager.getSignature(eid)
        systemManager.componentRemoved(eid, signature)
    }

    protected fun addSystem(system: GameSystem) {
        systemManager.addSystem(system)
        entityManager.registerEntitiesToSystem(system)
    }

    abstract fun update(t: Float, dt: Float)
    abstract fun draw(canvas: Canvas)
}

