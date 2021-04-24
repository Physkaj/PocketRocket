package com.example.pocketrocket.control

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder
import com.example.pocketrocket.utils.FPSToMillisPerFrame
import com.example.pocketrocket.utils.MillisToSec
import com.example.pocketrocket.utils.SecToMillis
import java.lang.Exception

class GameThread(val surfaceHolder: SurfaceHolder) : Thread() {
    var isRunning = false
        private set
    var targetMillisPerFrame: Float = 0f
    var targetFPS: Long = 60
        set(value) {
            field = value
            targetMillisPerFrame = FPSToMillisPerFrame(value)
        }
    var averageFPS: Float = 0f
        private set
    var targetMillisPerUpdate: Float = 0f
    var targetUPS: Long = 60
        set(value) {
            field = value
            targetMillisPerUpdate = FPSToMillisPerFrame(value)
        }
    var averageUPS: Float = 0f
        private set
    var onUpdate: (() -> Unit)? = null
    var onDraw: ((Canvas) -> Unit)? = null

    fun startThread() {
        isRunning = true
        start()
    }

    fun stopThread() {
        isRunning = false
        join()
    }

    override fun run() {
        var canvas: Canvas
        var currentTime: Long
        var elapsedTime: Long
        var sleepTime: Long
        var startMeasure: Long = System.currentTimeMillis()
        var frameCount = 0
        var updateCount = 0
        while (isRunning) {
            do {
                onUpdate?.invoke()
                ++updateCount
                currentTime = System.currentTimeMillis()
                elapsedTime = currentTime - startMeasure
                sleepTime = (updateCount * targetMillisPerUpdate - elapsedTime).toLong()
                // No render until the target UPS is met
            } while (sleepTime < 0)

            try {
                canvas = surfaceHolder.lockCanvas()
            } catch (e: Exception) {
                e.printStackTrace(); continue
            }
            // Draw everything
            onDraw?.invoke(canvas)
            // Sleep if too fast
            currentTime = System.currentTimeMillis()
            elapsedTime = currentTime - startMeasure
            sleepTime = (updateCount * targetMillisPerUpdate - elapsedTime).toLong()
            // If we are ahead we sleep to keep UPS constant
            if (sleepTime > 0)
                sleep(sleepTime)
            surfaceHolder.unlockCanvasAndPost(canvas)
            ++frameCount

            if (elapsedTime > SecToMillis(1L)) {
                averageUPS = updateCount / MillisToSec(elapsedTime)
                averageFPS = frameCount / MillisToSec(elapsedTime)
                Log.d("GameThread", "UPS: $averageUPS")
                Log.d("GameThread", "FPS: $averageFPS")
                updateCount = 0
                frameCount = 0
                startMeasure = elapsedTime - SecToMillis(1L) + System.currentTimeMillis()
            }
        }
    }
}