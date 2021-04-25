package com.example.pocketrocket.control

import android.view.SurfaceHolder
import com.example.pocketrocket.model.GameWorld

open class GameMaster(holder: SurfaceHolder) {
    private val gameThread = GameThread(holder)
    private var world: GameWorld? = null

    fun setupWorld(w: GameWorld) {
        world = w
        gameThread.targetFPS = 60
        gameThread.targetUPS = 1000L / world!!.tickMillis
        gameThread.onDraw = { world!!.draw(it) }
        gameThread.onUpdate = { world!!.update() }
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