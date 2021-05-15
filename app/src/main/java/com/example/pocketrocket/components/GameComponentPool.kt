package com.example.pocketrocket.components

import android.util.Log
import java.util.*

class GameComponentPool<T : IGameComponent>(preFill: Int = 0, private val recipe: () -> T) {
    private val everyComponent: MutableList<T> = mutableListOf<T>()
    private val availableComponents: Queue<T> = LinkedList<T>()
    val size: Int
        get() = everyComponent.size

    init {
        this.growPool(preFill)
    }

    fun getComponent(): T {
        if (availableComponents.size == 0)
            growPool(1)
        return remove()
    }

    private fun remove(): T {
        return availableComponents.remove()
    }

    fun returnComponent(obj: T) {
        availableComponents.add(obj)
    }

    fun growPool(n: Int) {
        for (i: Int in 0..(n - 1)) {
            val component = recipe.invoke()
            everyComponent.add(component)
            availableComponents.add(component)
        }
    }
}