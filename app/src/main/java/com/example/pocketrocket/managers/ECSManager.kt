package com.example.pocketrocket.managers

import android.graphics.Canvas
import com.example.pocketrocket.components.*
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.systems.GameSystem
import com.example.pocketrocket.utils.ScreenProperties
import java.util.*
import kotlin.reflect.KClass

interface ECSCallback {
    fun createEntity(): EidType
    fun destroyEntity(eid: EidType)
    fun getSignature(eid: EidType): BitSet
    fun hasComponent(eid: EidType, cid: CidType): Boolean
    fun <T : IGameComponent> getComponent(eid: EidType, cid: CidType): T
    fun <T : IGameComponent> addComponent(eid: EidType, cid: CidType): T
    fun getScreenProperties(): ScreenProperties
    fun getComponentPoolSize(cid: CidType): Int
    fun growComponentPoolSize(cid: CidType, size: Int)
}

abstract class ECSManager(protected val callbackGameManager: GameManagerCallback) : ECSCallback {

    override fun getScreenProperties() = callbackGameManager.getScreenProperties()

    fun resize(width: Int, height: Int) {
        val screenProperties = getScreenProperties()
        if (width == screenProperties.width && height == screenProperties.height) return
        // Do stuff with stuff that cares about the screen dimensions...
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

    override fun getComponentPoolSize(cid: CidType): Int {
        return componentManager.getComponentPoolSize(cid)
    }

    override fun growComponentPoolSize(cid: CidType, size: Int) {
        componentManager.growComponentPool(cid, size)
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