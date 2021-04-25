package com.example.pocketrocket.control

import android.content.Context
import android.os.SystemClock.sleep
import android.util.Log
import android.view.SurfaceHolder
import java.util.logging.Handler

/*
 * GameMaster controls both the game thread, the games assets and the game itself.
 * It is responsible of bridging the model and the view.
 */
abstract class GameMaster(holder: SurfaceHolder, private val context: Context) :
    SurfaceHolder.Callback {
    protected val gameClock = GameClock(holder)
    private var gameThread: Thread? = null
    var surfaceWidth: Int = 0
        private set
    var surfaceHeight: Int = 0
        private set

    init {
        holder.addCallback(this)
    }

    abstract fun setupGame()

    open fun destroyGame() {
        stopGame()
    }

    open fun startGame() {
        if (!gameClock.isRunning) {
            gameClock.start()
            gameThread = Thread(gameClock).also { it.start() }
        }
    }

    abstract fun resizeGame(width: Int, height: Int)

    open fun stopGame() {
        gameClock.stop()
        gameThread?.join()
        gameThread = null
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceWidth = holder.surfaceFrame.width()
        surfaceHeight = holder.surfaceFrame.height()
        setupGame()
        startGame()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        stopGame()
        surfaceWidth = width
        surfaceHeight = height
        Log.d("GM", "surfaceChanged: $width, $height")
        resizeGame(width, height)
        startGame()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        destroyGame()
    }
}