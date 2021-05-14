package com.example.pocketrocket.systems

import com.example.pocketrocket.managers.ECSCallback
import com.example.pocketrocket.entity.EidType
import java.util.*

abstract class GameSystem(val callback: ECSCallback) {
    protected val entityList: MutableSet<EidType> = mutableSetOf()
    fun hasEntity(eid: EidType): Boolean = entityList.contains(eid)
    fun addEntity(eid: EidType) = entityList.add(eid)
    fun removeEntity(eid: EidType) = entityList.remove(eid)
    fun clearEntities() = entityList.clear()
    abstract fun appliesToSignature(signature: BitSet): Boolean
}