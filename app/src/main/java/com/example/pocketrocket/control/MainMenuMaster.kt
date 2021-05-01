package com.example.pocketrocket.control

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.SurfaceHolder
import com.example.pocketrocket.model.StarField

class MainMenuMaster(h: SurfaceHolder, c: Context) : GameMaster(h, c) {
    companion object {
        val tickMillis: Long = 30L
    }

    private lateinit var starField: StarField
    private var isSetup = false
    private val debugPaint = Paint()

    init {
        debugPaint.style = Paint.Style.STROKE
        debugPaint.strokeWidth = 1f
        debugPaint.color = Color.GREEN
        debugPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD))
        debugPaint.textSize = 30f
    }

    override fun setupGame() {
        starField = StarField(surfaceWidth, surfaceHeight, tickMillis)
        gameClock.targetFPS = 60
        gameClock.targetMillisPerUpdate = tickMillis
        gameClock.onDraw = { this.draw(it) }
        gameClock.onTick = { starField.update() }
        isSetup = true
    }

    override fun startGame() {
        if (isSetup)
            super.startGame()
    }

    override fun resizeGame(width: Int, height: Int) {
        starField.resize(width, height)
    }

    private fun draw(canvas: Canvas) {
        starField.draw(canvas)
        drawDebugStats(canvas)
    }

    private fun drawDebugStats(canvas: Canvas) {
        debugPaint.style = Paint.Style.STROKE
        canvas.drawRect(
            1f,
            0f,
            (surfaceWidth - 1).toFloat(),
            (surfaceHeight - 2).toFloat(),
            debugPaint
        )
        debugPaint.style = Paint.Style.FILL
        canvas.drawText("FPS: ${gameClock.averageFPS}", 5f, 30f, debugPaint)
        canvas.drawText("UPS: ${gameClock.averageUPS}", 5f, 70f, debugPaint)
    }
}