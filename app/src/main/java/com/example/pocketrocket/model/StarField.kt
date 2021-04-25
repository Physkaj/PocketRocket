package com.example.pocketrocket.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import com.example.pocketrocket.control.GameMaster
import com.example.pocketrocket.utils.*
import kotlin.random.Random

class BackgroundStar(
    ori: Vec2D,
    pos: Vec2D = RadVec2D(),
    vel: Vec2D = RadVec2D(),
    var starRadius: Float = 5.0f
) : GameObject() {

    // Position relative to the origin
    var relPos: RadVec2D = pos.getRadVec2D()
    var origin: LinVec2D = ori.getLinVec2D()

    // Velocity relative to the origin
    var radVel = vel.getRadVec2D()
    var position: Vec2D
        get() = origin + relPos
        set(value) {
            relPos = (value - origin).getRadVec2D()
        }

    var starState = 0
    var stateDuration: Long = 0L
    var blinkPeriod: Long = 500L
    var starPaint: Paint = Paint()

    init {
        starPaint.color = Color.WHITE
    }

    override fun draw(c: Canvas) {
        when (starState) {
            0 -> c.drawCircle(
                origin.x + relPos.x,
                origin.y + relPos.y,
                starRadius * 1.1f,
                starPaint
            )
            1 -> c.drawCircle(
                origin.x + relPos.x,
                origin.y + relPos.y,
                starRadius * 0.9f,
                starPaint
            )
            else -> throw IllegalArgumentException("Illegal star state: $starState")
        }

        stateDuration += GameMaster.tickMillis
        if (stateDuration >= blinkPeriod) {
            starState = 1 - starState
            stateDuration %= blinkPeriod
        }

    }

    override fun update() {
        relPos.r += radVel.r
        relPos.arg += radVel.arg
    }
}

class StarField(w: Int, h: Int, t: Long) : GameWorld(w, h, t) {
    private val starsList: MutableList<BackgroundStar> = mutableListOf()
    private var nextSpawnTick: Int = 0
    private var maxRadius2 = width * width + height * height

    override fun reconfigure(width: Int, height: Int) {
        this.width = width
        this.height = height
        maxRadius2 = width * width + height * height
    }

    override fun draw(c: Canvas) {
        c.drawColor(Color.BLACK)
        val itr = starsList.iterator()
        while (itr.hasNext()) {
            itr.next().draw(c)
        }
    }

    override fun update() {
        val itr = starsList.iterator()
        while (itr.hasNext()) {
            val star = itr.next()
            if (star.relPos.r2 > maxRadius2) {
                itr.remove()
            } else {
                star.update()
            }
        }
        --nextSpawnTick
        if (nextSpawnTick < 0) {
            val star = BackgroundStar(
                ori = LinVec2D(0.5f * width, 0.5f * height) + RadVec2D().randVec(10f),
                pos = RadVec2D(0.0f, 0.0f) + RadVec2D().randVec(10f),
                vel = RadVec2D(0.05f * tickMillis, 0.0005f * tickMillis)
            )
            starsList.add(star)
            nextSpawnTick = Random.nextInt(100 / tickMillis.toInt(), 1000 / tickMillis.toInt())
        }
    }
}