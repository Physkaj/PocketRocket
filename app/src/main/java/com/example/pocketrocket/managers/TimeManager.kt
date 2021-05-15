package com.example.pocketrocket.managers

import android.os.SystemClock.sleep
import android.util.Log
import android.widget.Chronometer
import java.time.chrono.ChronoPeriod

const val nano: Float = 1e-9f
const val nanoInv: Long = 1000000000

class TimeManager(targetUPS: Float) : Runnable {
    private val targetNanoSecPerUpdate: Long = (1e9 / targetUPS).toLong()

    @Volatile
    var isRunning: Boolean = false
        private set
    var averageFPS: Float = 0f
        private set
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
                onUpdate?.invoke(nano * (timeStart + updateCount * targetNanoSecPerUpdate), nano * targetNanoSecPerUpdate)
                ++updateCount
                timeLeft = updateCount * targetNanoSecPerUpdate - (System.nanoTime() - timeStart)
                // No render until the target UPS is met
            } while (false) // timeLeft < 0) This messes up debugging, set a debug flag?
            // Draw everything
            onDraw?.invoke()
            ++frameCount
            // Sleep if too fast
            timeLeft = updateCount * targetNanoSecPerUpdate - (System.nanoTime() - timeStart)
            // If we are ahead we sleep to keep UPS constant
            if (timeLeft > 0)
                sleep(timeLeft / 1000000) // Convert to ms

            val secondsElapsed = nano * (System.nanoTime() - timeStart)
            if (secondsElapsed > 2) {
                averageUPS = updateCount / secondsElapsed
                averageFPS = frameCount / secondsElapsed
                Log.d("GameClock", "UPS: $averageUPS")
                Log.d("GameClock", "FPS: $averageFPS")
                updateCount = 0
                frameCount = 0
                timeStart = System.nanoTime()
            }
        }
    }
}