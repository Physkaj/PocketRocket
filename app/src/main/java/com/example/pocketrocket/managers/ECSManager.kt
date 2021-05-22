package com.example.pocketrocket.managers

import android.graphics.Canvas
import com.example.pocketrocket.components.*
import com.example.pocketrocket.entity.EidType
import com.example.pocketrocket.systems.GameSystem
import com.example.pocketrocket.utils.ScreenProperties
import com.example.pocketrocket.utils.ThreadProperties
import java.util.*
import kotlin.reflect.KClass

interface ECSCallback {
    fun createEntity(): EidType
    fun destroyEntity(eid: EidType)
    fun getSignature(eid: EidType): BitSet
    fun hasComponent(eid: EidType, cid: CidType): Boolean
    fun <T : IGameComponent> getComponent(eid: EidType, cid: CidType): T
    fun <T : IGameComponent> getComponentOrNull(eid: EidType, cid: CidType): T?
    fun <T : IGameComponent> addComponent(eid: EidType, cid: CidType): T
    fun getComponentPoolSize(cid: CidType): Int
    fun growComponentPoolSize(cid: CidType, size: Int)

    fun getScreenProperties(): ScreenProperties
    fun getThreadProperties(): ThreadProperties
}

abstract class ECSManager(protected val callbackGameManager: GameManagerCallback) : ECSCallback {

    override fun getScreenProperties() = callbackGameManager.getScreenProperties()
    override fun getThreadProperties() = callbackGameManager.getThreadProperties()

    open fun onResize(width: Int, height: Int) {}
    fun resize(width: Int, height: Int) {
        val screenProperties = getScreenProperties()
        if (width == screenProperties.width && height == screenProperties.height) return
        // Do stuff with stuff that cares about the screen dimensions...
        onResize(width, height)
    }

    private val entityManager = EntityManager()
    private val componentManager = ComponentManager()
    private val systemManager = SystemManager()

    override fun createEntity(): EidType = entityManager.createEntity()

    override fun destroyEntity(eid: EidType) {
        val signature = entityManager.getSignature(eid)
        entityManager.destroyEntity(eid)
        componentManager.entityDestroyed(eid, signature)
        systemManager.entityDestroyed(eid)
    }

    override fun getSignature(eid: EidType): BitSet = entityManager.getSignature(eid)

    override fun hasComponent(eid: EidType, cid: CidType): Boolean = entityManager.getSignature(eid)[cid]

    override fun <T : IGameComponent> getComponent(eid: EidType, cid: CidType): T = componentManager.getComponent<T>(eid, cid)

    override fun <T : IGameComponent> getComponentOrNull(eid: EidType, cid: CidType): T? = componentManager.getComponentOrNull<T>(eid, cid)

    protected fun registerComponent(componentClass: KClass<out IGameComponent>) = componentManager.registerComponent(componentClass)

    override fun <T : IGameComponent> addComponent(eid: EidType, cid: CidType): T {
        val component = componentManager.addComponent<T>(eid, cid)
        val signature = entityManager.setSignature(eid, cid, true)
        systemManager.componentAdded(eid, signature)
        return component
    }

    override fun getComponentPoolSize(cid: CidType): Int = componentManager.getComponentPoolSize(cid)

    override fun growComponentPoolSize(cid: CidType, size: Int) = componentManager.growComponentPool(cid, size)

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