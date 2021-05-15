package com.example.pocketrocket.utils

import java.lang.Math.cbrt
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

class Vec3D(var x: Float, var y: Float, var z: Float) {
    val r: Float
        get() = sqrt(r2)
    val r2: Float
        get() = x * x + y * y + z * z
    val rho: Float
        get() = sqrt(rho2)
    val rho2: Float
        get() = x * x + y * y
    val azimuth: Float
        get() = atan2(y, x)
    val inclination: Float
        get() = acos(z / r)

    operator fun unaryPlus() = Vec3D(x, y, z)
    operator fun unaryMinus() = Vec3D(-x, -y, -z)
    operator fun plus(vec: Vec3D): Vec3D = Vec3D(this.x + vec.x, this.y + vec.y, this.z + z)
    operator fun minus(vec: Vec3D): Vec3D = Vec3D(this.x - vec.x, this.y - vec.y, this.z - z)
    operator fun times(vec: Vec3D): Float = dot(vec)
    fun dot(vec: Vec3D): Float = this.x * vec.x + this.y * vec.y + this.z * vec.z
    operator fun times(factor: Float): Vec3D = Vec3D(x * factor, y * factor, z * factor)
}

fun Vec2D.createRandomVecLin(x1: Float, y1: Float, x2: Float, y2: Float): Vec2D =
    Vec2D(x1 + Random.nextFloat() * (x2 - x1), y1 + Random.nextFloat() * (y2 - y1))

fun Vec2D.createRandomVecLin(v1: Vec2D, v2: Vec2D): Vec2D = createRandomVecLin(v1.x, v1.y, v2.x, v2.y)
fun Vec2D.createVecPolar(r: Float, arg: Float): Vec2D = Vec2D(r * cos(arg), r * sin(arg))
fun Vec2D.createRandomVecPolar(r1: Float, r2: Float, a1: Float = -onePI, a2: Float = onePI): Vec2D =
    createVecPolar(sqrt(Random.nextFloat() * (r2 * r2 - r1 * r1) + r1 * r1), a1 + Random.nextFloat() * (a2 - a1))

fun Vec2D.randVec(r: Float): Vec2D = createRandomVecPolar(0f, r)

fun Vec3D.createRandomVecLin(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): Vec3D =
    Vec3D(x1 + Random.nextFloat() * (x2 - x1), y1 + Random.nextFloat() * (y2 - y1), z1 + Random.nextFloat() * (z2 - z1))

fun Vec3D.createRandomVecLin(v1: Vec3D, v2: Vec3D): Vec3D = createRandomVecLin(v1.x, v2.x, v1.y, v2.y, v1.z, v2.z)
fun Vec3D.createVecPolar(r: Float, inclination: Float, azimuth: Float): Vec3D =
    Vec3D(r * cos(inclination) * cos(azimuth), r * cos(inclination) * sin(azimuth), r * sin(inclination))

fun Vec3D.createRandomVecPolar(r1: Float, r2: Float, i1: Float = 0f, i2: Float, a1: Float = -onePI, a2: Float = onePI): Vec3D {
    val r1cubed = r1 * r1 * r1
    val r2cubed = r2 * r2 * r2
    val r = cbrt(Random.nextDouble() * (r2cubed - r1cubed) + r1cubed).toFloat()
    val s1 = sin(i1)
    val s2 = sin(i2)
    val randSin = Random.nextFloat() * (s1 - s2) + s1
    return createVecPolar(r, asin(randSin), a1 + Random.nextFloat() * (a2 - a1))
}

fun Vec3D.randVec(r: Float): Vec3D =
    createVecPolar(cbrt(Random.nextDouble() * r * r * r).toFloat(), asin(1 - 2 * Random.nextFloat()), Random.nextFloat() * twoPI)
