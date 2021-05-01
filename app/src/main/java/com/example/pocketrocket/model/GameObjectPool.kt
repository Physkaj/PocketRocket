package com.example.pocketrocket.model

import android.util.Log
import java.util.*

class GameObjectPool<T : GameObjectPooled>(preFill: Int = 0, private val recipe: () -> T) {
    private val queue: Queue<T> = LinkedList<T>()
    private val allItems = mutableListOf<T>()

    init {
        this.growPool(preFill)
    }

    fun add(obj: T) {
        queue.add(obj)
    }

    fun getFromPool(): T {
        if (queue.size == 0)
            growPool(1)
        return remove()
    }

    fun remove(): T {
        return queue.remove()
    }

    fun returnToPool(obj: T) {
        queue.add(obj)
    }

    fun growPool(n: Int) {
        for (i: Int in 0..(n - 1))
            queue.add(recipe.invoke().also {
                @Suppress("UNCHECKED_CAST")
                it.pool = this as GameObjectPool<GameObjectPooled>
                this.allItems.add(it)
            })
    }
}