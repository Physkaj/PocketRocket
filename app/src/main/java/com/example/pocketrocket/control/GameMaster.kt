package com.example.pocketrocket.control

import android.view.SurfaceHolder
import com.example.pocketrocket.model.GameWorld

open class GameMaster(holder: SurfaceHolder) {
    private val gameThread = GameThread(holder)
    private var world: GameWorld? = null

    companion object {
        private var instance: GameMaster? = null
        val tickMillis: Long
            get() = instance?.world?.tickMillis ?: 0L
    }

    init {
        if (instance != null)
            throw InstantiationException("GameMaster is a singleton")
        instance = this
    }

    fun setupWorld(w: GameWorld) {
        world = w
        gameThread.targetFPS = 60
        gameThread.targetMillisPerUpdate = world!!.tickMillis
        gameThread.onDraw = { world!!.draw(it) }
        gameThread.onUpdate = { world!!.update() }
    }

    fun destroyWorld() {
        stopWorld()
        world = null
    }

    fun startWorld() {
        if (world != null && !gameThread.isRunning)
            gameThread.startThread()
    }

    fun reconfigureWorld(width: Int, height: Int) {
        world?.reconfigure(width, height)
    }

    fun stopWorld() {
        gameThread.stopThread()
    }
}