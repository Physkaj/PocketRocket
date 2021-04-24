package com.example.pocketrocket.utils

import kotlin.math.*
import kotlin.random.Random

const val onePI = PI.toFloat()
const val twoPI = 2 * PI.toFloat()

interface Vec2D {
    val x: Float
    val y: Float
    val r: Float
    val r2: Float
    val arg: Float
    fun getLinVec2D() = LinVec2D(x, y)
    fun getRadVec2D() = RadVec2D(r, arg)
    operator fun unaryPlus(): Vec2D
    operator fun unaryMinus(): Vec2D
    operator fun plus(vec: Vec2D): LinVec2D = LinVec2D(this.x + vec.x, this.y + vec.y)
    operator fun minus(vec: Vec2D): LinVec2D = LinVec2D(this.x - vec.x, this.y - vec.y)
    operator fun times(vec: Vec2D): Float = dot(vec)
    fun dot(vec: Vec2D): Float = this.x * vec.x + this.y * vec.y
}

data class LinVec2D(override var x: Float = 0.0f, override var y: Float = 0.0f) : Vec2D {
    override val r: Float
        get() = sqrt(r2)
    override val r2: Float
        get() = x * x + y * y
    override val arg: Float
        get() = atan2(y, x)

    override fun getLinVec2D(): LinVec2D = this
    override operator fun unaryPlus() = LinVec2D(x, y)
    override operator fun unaryMinus() = LinVec2D(-x, -y)
    operator fun times(mult: Float): LinVec2D = LinVec2D(x * mult, y * mult)
}

data class RadVec2D(override var r: Float = 0.0f, override var arg: Float = 0.0f) : Vec2D {
    override val x: Float
        get() = r * cos(arg)
    override val y: Float
        get() = r * sin(arg)
    override val r2: Float
        get() = r * r

    override fun getRadVec2D(): RadVec2D = this
    override operator fun unaryPlus() = RadVec2D(r, arg)
    override operator fun unaryMinus() = RadVec2D(r, arg + onePI)
    operator fun times(mult: Float): RadVec2D = RadVec2D(r * mult, arg)
    fun valid(): Boolean = r > 0 && -onePI <= arg && arg <= onePI
    fun makeValid() {
        // Make r > 0
        arg += if (r >= 0) 0.0f else onePI
        r = abs(r)
        // Make argument to be in [-pi,pi]
        arg = arg.rem(twoPI)
        if (arg > onePI) {
            arg -= twoPI
        } else if (arg < onePI) {
            arg += twoPI
        }
    }
}

fun LinVec2D.randVec(x1: Float, y1: Float, x2: Float, y2: Float): LinVec2D {
    this.x = x1 + Random.nextFloat() * (x2 - x1)
    this.y = y1 + Random.nextFloat() * (y2 - y1)
    return this
}

fun LinVec2D.randVec(v1: LinVec2D, v2: LinVec2D): LinVec2D {
    return this.randVec(v1.x, v2.x, v1.y, v2.y)
}

fun RadVec2D.randVec(r1: Float, r2: Float, a1: Float = -onePI, a2: Float = onePI): RadVec2D {
    r = sqrt(Random.nextFloat() * (r2 * r2 - r1 * r1) + r1 * r1)
    arg = a1 + Random.nextFloat() * (a2 - a1)
    return this
}

fun RadVec2D.randVec(v1: RadVec2D, v2: RadVec2D): RadVec2D =
    this.randVec(v1.r, v2.r, v1.arg, v2.arg)

fun RadVec2D.randVec(r: Float): RadVec2D = this.randVec(r1 = 0f, r2 = r)
