package com.example.pocketrocket.control

import android.graphics.Canvas
import android.os.SystemClock.sleep
import android.util.Log
import android.view.SurfaceHolder
import com.example.pocketrocket.utils.FPSToMillisPerFrame
import com.example.pocketrocket.utils.millisToSec
import com.example.pocketrocket.utils.secToMillis
import java.lang.Exception

class GameClock(val surfaceHolder: SurfaceHolder) : Runnable {
    @Volatile
    var isRunning: Boolean = false
        private set
    var targetMillisPerFrame: Float = 0f
    var targetFPS: Long = 60
        set(value) {
            field = value
            targetMillisPerFrame = FPSToMillisPerFrame(value)
        }
    var averageFPS: Float = 0f
        private set
    var targetMillisPerUpdate: Long = 0L
    var averageUPS: Float = 0f
        private set
    var onTick: (() -> Unit)? = null
    var onDraw: ((Canvas) -> Unit)? = null

    fun start() {
        isRunning = true
    }

    fun stop() {
        isRunning = false
    }

    override fun run() {
        var canvas: Canvas
        var currentMillis: Long
        var elapsedMillis: Long
        var sleepMillis: Long
        var startMillis: Long = System.currentTimeMillis()
        var frameCount = 0
        var updateCount = 0
        while (isRunning) {
            do {
                onTick?.invoke()
                ++updateCount
                currentMillis = System.currentTimeMillis()
                elapsedMillis = currentMillis - startMillis
                sleepMillis = updateCount * targetMillisPerUpdate - elapsedMillis
                // No render until the target UPS is met
            } while (sleepMillis < 0)

            try {
                canvas = surfaceHolder.lockCanvas()
            } catch (e: Exception) {
                e.printStackTrace(); continue
            }
            // Draw everything
            onDraw?.invoke(canvas)
            // Sleep if too fast
            currentMillis = System.currentTimeMillis()
            elapsedMillis = currentMillis - startMillis
            sleepMillis = updateCount * targetMillisPerUpdate - elapsedMillis
            // If we are ahead we sleep to keep UPS constant
            if (sleepMillis > 0)
                sleep(sleepMillis)
            surfaceHolder.unlockCanvasAndPost(canvas)
            ++frameCount
            currentMillis = System.currentTimeMillis()
            elapsedMillis = currentMillis - startMillis
            if (updateCount >= secToMillis(2) / targetMillisPerUpdate) {
                averageUPS = updateCount / millisToSec(elapsedMillis)
                averageFPS = frameCount / millisToSec(elapsedMillis)
                Log.d("GameClock", "UPS: $averageUPS")
                Log.d("GameClock", "FPS: $averageFPS")
                updateCount = 0
                frameCount = 0
                startMillis = System.currentTimeMillis()
            }
        }
    }
}