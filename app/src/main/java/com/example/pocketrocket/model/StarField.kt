package com.example.pocketrocket.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Picture
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.util.Log
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.example.pocketrocket.R
import com.example.pocketrocket.control.GameMaster
import com.example.pocketrocket.control.MainMenuMaster
import com.example.pocketrocket.utils.*
import kotlin.math.abs
import kotlin.random.Random

class BackgroundStar(
    ori: Vec2D = LinVec2D(),
    pos: Vec2D = RadVec2D(),
    vel: Vec2D = RadVec2D(),
    sr: Float = 10.0f
) : GameObject(), GameObjectPooled {
    override var pool: GameObjectPool<GameObjectPooled>? = null
        get() = field
        set(value) {
            if (pool != null)
                throw RuntimeException("Objects of ${this.javaClass.name} + should never change pool.")
            field = value
        }

    // Position relative to the origin
    lateinit var relPos: RadVec2D
    lateinit var origin: LinVec2D

    // Velocity relative to the origin
    lateinit var radVel: RadVec2D

    // Star radius
    private var starRadius: Float = 0f
    var position: Vec2D
        get() = origin + relPos
        set(value) {
            relPos = (value - origin).getRadVec2D()
        }

    var stateDuration: Long = 0L
    var blinkPeriod: Long = 500L
    var starPaint: Paint = Paint()

    init {
        initialise(ori, pos, vel, sr)
        starPaint.color = Color.WHITE
    }

    fun initialise(
        ori: Vec2D = LinVec2D(),
        pos: Vec2D = RadVec2D(),
        vel: Vec2D = RadVec2D(),
        sr: Float = 10.0f
    ) {
        relPos = pos.getRadVec2D()
        origin = ori.getLinVec2D()
        radVel = vel.getRadVec2D()
        starRadius = sr
    }

    fun destroy() {
        pool?.returnToPool(this)
    }

    override fun draw(c: Canvas) {
        val factor: Float = 0.75f + 0.5f * abs(1f - 2 * stateDuration / blinkPeriod.toFloat())
        c.drawCircle(
            origin.x + relPos.x,
            origin.y + relPos.y,
            starRadius * factor,
            starPaint
        )

        stateDuration += MainMenuMaster.tickMillis
        if (stateDuration >= blinkPeriod)
            stateDuration %= blinkPeriod
    }

    override fun update() {
        relPos.r += radVel.r
        relPos.arg += radVel.arg
    }
}

class StarField(var width: Int, var height: Int, var tickMillis: Long) : GameObject() {
    private val starsList: MutableList<BackgroundStar> = mutableListOf()
    private var nextSpawnTick: Int = 0
    private var maxRadius2 = width * width + height * height
    private val starPool = GameObjectPool<BackgroundStar>() { BackgroundStar() }

    fun resize(width: Int, height: Int) {
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
                star.destroy()
                itr.remove()
            } else {
                star.update()
            }
        }
        --nextSpawnTick
        if (nextSpawnTick < 0) {
            val star = starPool.getFromPool()
            star.initialise(
                ori = LinVec2D(0.5f * width, 0.5f * height) + RadVec2D().randVec(10f),
                pos = RadVec2D(0.0f, 0.0f) + RadVec2D().randVec(10f),
                vel = RadVec2D(0.05f * tickMillis, 0.0005f * tickMillis),
                sr = 10f
            )
            starsList.add(star)
            nextSpawnTick = Random.nextInt(500 / tickMillis.toInt(), 2000 / tickMillis.toInt())
        }
    }
}