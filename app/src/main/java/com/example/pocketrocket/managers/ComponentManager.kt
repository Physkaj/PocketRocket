package com.example.pocketrocket.managers

import com.example.pocketrocket.components.CidType
import com.example.pocketrocket.components.GameComponentCompanion
import com.example.pocketrocket.components.GameComponentPool
import com.example.pocketrocket.components.IGameComponent
import com.example.pocketrocket.entity.EidType
import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance

class ComponentManager {
    private val componentArrays = Vector<MutableMap<EidType, IGameComponent>>()
    private val componentPools = Vector<GameComponentPool<IGameComponent>>()

    fun registerComponent(componentClass: KClass<out IGameComponent>) {
        val componentCompanion = componentClass.companionObjectInstance as? GameComponentCompanion
            ?: throw IllegalArgumentException("Component ${componentClass.simpleName} has no Companion implementing ComponentCompanion")
        val cid = componentCompanion.componentID
        componentArrays.insertElementAt(mutableMapOf<EidType, IGameComponent>(), cid)
        componentPools.insertElementAt(GameComponentPool() { componentClass.createInstance() }, cid)
    }

    fun entityDestroyed(eid: EidType, signature: BitSet) {
        for (cid in 0 until componentArrays.size)
            if (signature[cid])
                removeComponent(eid, cid)
    }

    fun <T : IGameComponent> addComponent(eid: EidType, cid: CidType): T {
        val component = componentPools[cid].getComponent() as T
        componentArrays[cid][eid] = component
        return component
    }

    fun removeComponent(eid: EidType, cid: CidType) {
        val component = componentArrays[cid].remove(eid)
            ?: throw NoSuchElementException("Entity: $eid does not have a component $cid")
        componentPools[cid].returnComponent(component)
    }

    fun <T : IGameComponent> getComponent(eid: EidType, cid: CidType): T {
        val map =
            componentArrays.elementAtOrElse(cid) { throw NoSuchElementException("No registered component with CID: $cid") } as? Map<EidType, T>
                ?: throw ClassCastException("Could not cast into Map<TypeEID,T($cid)>")
        return map[eid]
            ?: throw (NoSuchElementException("Entity: $eid does not have a component $cid"))
    }

    fun getComponentPoolSize(cid: CidType): Int {
        return componentPools[cid].size
    }

    fun growComponentPool(cid: CidType, size: Int) {
        val pool = componentPools[cid]
        val increase = max(0, size - pool.size)
        componentPools[cid].growPool(increase)
    }

}