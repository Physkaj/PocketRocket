package com.example.pocketrocket.managers

import android.os.SystemClock.sleep
import android.util.Log

const val nano: Float = 1e-9f
const val nanoInv: Long = 1000000000

class TimeManager(tickSec: Int = 30) : Runnable {
    @Volatile
    var isRunning: Boolean = false
        private set
    var averageFPS: Float = 0f
        private set
    val targetNanoSecPerUpdate: Long = tickSec * nanoInv
    var averageUPS: Float = 0f
        private set
    var onUpdate: ((Float, Float) -> Unit)? = null
    var onDraw: (() -> Unit)? = null

    fun stop() {
        isRunning = false
    }

    override fun run() {
        isRunning = true
        var timeLeft: Long
        var timeStart: Long = System.nanoTime()
        var frameCount = 0
        var updateCount = 0
        while (isRunning) {
            do {
                onUpdate?.invoke((updateCount * targetNanoSecPerUpdate).toFloat(), nano * targetNanoSecPerUpdate)
                ++updateCount
                timeLeft = updateCount * targetNanoSecPerUpdate - (System.nanoTime() - timeStart)
                // No render until the target UPS is met
            } while (timeLeft < 0)
            // Draw everything
            onDraw?.invoke()
            ++frameCount
            // Sleep if too fast
            timeLeft = updateCount * targetNanoSecPerUpdate - (System.nanoTime() - timeStart)
            // If we are ahead we sleep to keep UPS constant
            if (timeLeft > 0)
                sleep(timeLeft)

            val updatesPerInterval = 2 / (nano * targetNanoSecPerUpdate)
            if (updateCount >= updatesPerInterval) {
                val secondsElapsed = nano * (System.nanoTime() - timeStart)
                averageUPS = updateCount / (nano * secondsElapsed)
                averageFPS = frameCount / (nano * secondsElapsed)
                Log.d("GameClock", "UPS: $averageUPS")
                Log.d("GameClock", "FPS: $averageFPS")
                updateCount = 0
                frameCount = 0
                timeStart = System.nanoTime()
            }
        }
    }
}