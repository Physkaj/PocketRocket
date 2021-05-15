package com.example.pocketrocket.utils

import kotlin.math.*
import kotlin.random.Random

const val onePI = PI.toFloat()
const val twoPI = 2 * PI.toFloat()

class Vec2D(var x: Float = 0f, var y: Float = 0f) {
    val r: Float
        get() = sqrt(r2)
    val r2: Float
        get() = x * x + y * y
    val arg: Float
        get() = atan2(y, x)

    operator fun unaryPlus() = Vec2D(x, y)
    operator fun unaryMinus() = Vec2D(-x, -y)
    operator fun plus(vec: Vec2D): Vec2D = Vec2D(this.x + vec.x, this.y + vec.y)
    operator fun minus(vec: Vec2D): Vec2D = Vec2D(this.x - vec.x, this.y - vec.y)
    operator fun times(vec: Vec2D): Float = dot(vec)
    fun dot(vec: Vec2D): Float = this.x * vec.x + this.y * vec.y
    operator fun times(factor: Float): Vec2D = Vec2D(x * factor, y * factor)
}

fun Vec2D.createRandomVecLin(x1: Float, y1: Float, x2: Float, y2: Float): Vec2D =
    Vec2D(x1 + Random.nextFloat() * (x2 - x1), y1 + Random.nextFloat() * (y2 - y1))

fun Vec2D.createRandomVecLin(v1: Vec2D, v2: Vec2D): Vec2D = createRandomVecLin(v1.x, v2.x, v1.y, v2.y)
fun Vec2D.createVecPolar(r: Float, arg: Float): Vec2D = Vec2D(r * cos(arg), r * sin(arg))
fun Vec2D.createRandomVecPolar(r1: Float, r2: Float, a1: Float = -onePI, a2: Float = onePI): Vec2D =
    createVecPolar(sqrt(Random.nextFloat() * (r2 * r2 - r1 * r1) + r1 * r1), a1 + Random.nextFloat() * (a2 - a1))

fun Vec2D.randVec(r: Float): Vec2D = createRandomVecPolar(0f, r)
