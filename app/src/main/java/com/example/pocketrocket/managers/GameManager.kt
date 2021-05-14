package com.example.pocketrocket.managers

import android.content.Context
import android.view.SurfaceHolder
import java.lang.Exception

class GameManager(private val context: Context, private val surfaceHolder: SurfaceHolder, private val ecsManager: ECSManager) :
    SurfaceHolder.Callback {
    private val timeManager = TimeManager(30)

    init {
        timeManager.onUpdate = this::onUpdate
        timeManager.onDraw = this::onDraw
    }

    private var gameThread: Thread? = null

    init {
        surfaceHolder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startGame()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopGame()
    }

    // It takes some time to start the thread so do not check if it is running immediately after
    private fun startGame() {
        if (!timeManager.isRunning)
            gameThread = Thread(timeManager).also { it.start() }
    }

    private fun stopGame() {
        timeManager.stop()
    }

    private fun onUpdate(t: Float, dt: Float) {
        ecsManager.update(t, dt)
    }

    private fun onDraw() {
        val canvas = try {
            surfaceHolder.lockCanvas()
        } catch (e: Exception) {
            e.printStackTrace(); return
        }
        if (canvas.width > 0 && canvas.height > 0) {
            ecsManager.draw(canvas)
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }
}