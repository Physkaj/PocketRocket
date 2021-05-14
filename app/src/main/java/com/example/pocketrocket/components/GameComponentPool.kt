package com.example.pocketrocket.components

import java.util.*

class GameComponentPool<T : IGameComponent>(preFill: Int = 0, private val recipe: () -> T) {
    private val queue: Queue<T> = LinkedList<T>()

    init {
        this.growPool(preFill)
    }

    fun add(obj: T) {
        queue.add(obj)
    }

    fun getComponent(): T {
        if (queue.size == 0)
            growPool(1)
        return remove()
    }

    fun remove(): T {
        return queue.remove()
    }

    fun returnComponent(obj: T) {
        queue.add(obj)
    }

    fun growPool(n: Int) {
        for (i: Int in 0..(n - 1))
            queue.add(recipe.invoke())
    }
}