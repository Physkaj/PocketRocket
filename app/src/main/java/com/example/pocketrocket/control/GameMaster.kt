package com.example.pocketrocket.control

import android.view.SurfaceHolder
import com.example.pocketrocket.model.GameWorld

open class GameMaster(holder: SurfaceHolder, var world: GameWorld) {
    private val gameThread = GameThread(holder)

    init {
        gameThread.targetFPS = 60
        gameThread.targetUPS = 10
        gameThread.onDraw = { world.draw(it) }
        gameThread.onUpdate = { world.update() }
    }

    fun startWorld() {
        if (!gameThread.isRunning)
            gameThread.startThread()
    }

    fun reconfigureWorld(width: Int, height: Int) {
        world.reconfigure(width, height)
    }

    fun stopWorld() {
        gameThread.stopThread()
    }
}