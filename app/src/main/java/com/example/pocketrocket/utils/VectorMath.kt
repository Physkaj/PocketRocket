package com.example.pocketrocket.utils

import java.lang.Math.cbrt
import kotlin.math.*
import kotlin.random.Random

const val onePI = PI.toFloat()
const val twoPI = 2 * PI.toFloat()

data class Vec2D(var x: Float = 0f, var y: Float = 0f) {
    constructor(x: Number, y: Number) : this(x.toFloat(), y.toFloat())

    companion object {
        fun createRandomVecLin(x1: Float, y1: Float, x2: Float, y2: Float): Vec2D = Vec2D(
            x1 + Random.nextFloat() * (x2 - x1),
            y1 + Random.nextFloat() * (y2 - y1)
        )

        fun createRandomVecLin(v1: Vec2D, v2: Vec2D): Vec2D = createRandomVecLin(v1.x, v1.y, v2.x, v2.y)
        fun createVecPolar(r: Float, arg: Float): Vec2D = Vec2D(r * cos(arg), r * sin(arg))
        fun createRandomVecPolar(r1: Float, r2: Float, a1: Float = -onePI, a2: Float = onePI): Vec2D = createVecPolar(
            sqrt(Random.nextFloat() * (r2 * r2 - r1 * r1) + r1 * r1),
            a1 + Random.nextFloat() * (a2 - a1)
        )

        fun randVec(r: Float): Vec2D = createRandomVecPolar(0f, r)
    }

    val r: Float
        get() = sqrt(r2)
    val r2: Float
        get() = x * x + y * y
    val arg: Float
        get() = atan2(y, x)

    operator fun get(i: Int): Float {
        return when (i) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("Invalid index: $i for a 2D vector.")
        }
    }

    operator fun set(i: Int, value: Float) {
        when (i) {
            0 -> x = value
            1 -> y = value
            else -> throw IndexOutOfBoundsException("Invalid index: $i for a 2D vector.")
        }
    }

    fun clear() {
        x = 0f; y = 0f; }

    operator fun unaryPlus() = Vec2D(x, y)
    operator fun unaryMinus() = Vec2D(-x, -y)
    operator fun plus(vec: Vec2D): Vec2D = Vec2D(this.x + vec.x, this.y + vec.y)
    operator fun plusAssign(vec: Vec2D) {
        this.x += vec.x
        this.y += vec.y
    }

    operator fun minus(vec: Vec2D): Vec2D = Vec2D(this.x - vec.x, this.y - vec.y)
    operator fun minusAssign(vec: Vec2D) {
        this.x -= vec.x
        this.y -= vec.y
    }

    operator fun times(vec: Vec2D): Float = dot(vec)
    fun dot(vec: Vec2D): Float = this.x * vec.x + this.y * vec.y
    operator fun times(factor: Number): Vec2D = Vec2D(x * factor.toFloat(), y * factor.toFloat())
    operator fun timesAssign(factor: Number) {
        this.x *= factor.toFloat()
        this.y *= factor.toFloat()
    }

    operator fun div(factor: Number): Vec2D = Vec2D(x / factor.toFloat(), y / factor.toFloat())
    operator fun divAssign(factor: Number) = timesAssign(1f / factor.toFloat())
    fun plusElementwise(vec: Vec2D): Vec2D = Vec2D(this.x + vec.x, this.y + vec.y)
    fun minusElementwise(vec: Vec2D): Vec2D = Vec2D(this.x - vec.x, this.y - vec.y)
    fun timesElementwise(vec: Vec2D): Vec2D = Vec2D(this.x * vec.x, this.y * vec.y)
    fun divElementwise(vec: Vec2D): Vec2D = Vec2D(this.x / vec.x, this.y / vec.y)
    fun rotate(angle: Float) {
        val tmpx = x * cos(angle) + y * sin(angle)
        y = -x * sin(angle) + y * cos(angle)
        x = tmpx
    }
}

operator fun Number.times(vec: Vec2D): Vec2D = vec.times(this)

data class Vec3D(var x: Float, var y: Float, var z: Float) {
    constructor(x: Number, y: Number, z: Number) : this(x.toFloat(), y.toFloat(), z.toFloat())

    companion object {
        fun Vec3D.createRandomVecLin(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): Vec3D = Vec3D(
            x1 + Random.nextFloat() * (x2 - x1),
            y1 + Random.nextFloat() * (y2 - y1),
            z1 + Random.nextFloat() * (z2 - z1)
        )

        fun Vec3D.createRandomVecLin(v1: Vec3D, v2: Vec3D): Vec3D = createRandomVecLin(v1.x, v2.x, v1.y, v2.y, v1.z, v2.z)
        fun Vec3D.createVecPolar(r: Float, inclination: Float, azimuth: Float): Vec3D = Vec3D(
            r * cos(inclination) * cos(azimuth),
            r * cos(inclination) * sin(azimuth),
            r * sin(inclination)
        )

        fun Vec3D.createRandomVecPolar(r1: Float, r2: Float, i1: Float = 0f, i2: Float, a1: Float = -onePI, a2: Float = onePI): Vec3D {
            val r1cubed = r1 * r1 * r1
            val r2cubed = r2 * r2 * r2
            val r = cbrt(Random.nextDouble() * (r2cubed - r1cubed) + r1cubed).toFloat()
            val s1 = sin(i1)
            val s2 = sin(i2)
            val randSin = Random.nextFloat() * (s1 - s2) + s1
            return createVecPolar(r, asin(randSin), a1 + Random.nextFloat() * (a2 - a1))
        }

        fun Vec3D.randVec(r: Float): Vec3D = createVecPolar(
            cbrt(Random.nextDouble() * r * r * r).toFloat(),
            asin(1 - 2 * Random.nextFloat()),
            Random.nextFloat() * twoPI
        )
    }

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
        get() = if (r == 0f) 0f else acos(z / r)

    operator fun get(i: Int): Float {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IndexOutOfBoundsException("Invalid index: $i for a 3D vector.")
        }
    }

    operator fun set(i: Int, value: Float) {
        when (i) {
            0 -> x = value
            1 -> y = value
            2 -> z = value
            else -> throw IndexOutOfBoundsException("Invalid index: $i for a 3D vector.")
        }
    }

    fun clear() {
        x = 0f; y = 0f; z = 0f
    }

    operator fun unaryPlus() = Vec3D(x, y, z)
    operator fun unaryMinus() = Vec3D(-x, -y, -z)
    operator fun plus(vec: Vec3D): Vec3D = Vec3D(this.x + vec.x, this.y + vec.y, this.z + vec.z)
    operator fun plusAssign(vec: Vec3D) {
        this.x += vec.x
        this.y += vec.y
        this.z += vec.z
    }

    operator fun minus(vec: Vec3D): Vec3D = Vec3D(this.x - vec.x, this.y - vec.y, this.z - vec.z)
    operator fun minusAssign(vec: Vec3D) {
        this.x -= vec.x
        this.y -= vec.y
        this.z -= vec.z
    }

    operator fun times(vec: Vec3D): Float = dot(vec)
    operator fun timesAssign(vec: Vec3D) {
        this.x *= vec.x
        this.y *= vec.y
        this.z *= vec.z
    }

    fun dot(vec: Vec3D): Float = this.x * vec.x + this.y * vec.y + this.z * vec.z
    fun cross(v2: Vec3D): Vec3D = Vec3D(this.y * v2.z - v2.y * this.z, this.z * v2.x - v2.z * this.x, this.x * v2.y - v2.x * this.y)
    operator fun times(factor: Number): Vec3D = Vec3D(x * factor.toFloat(), y * factor.toFloat(), z * factor.toFloat())
    operator fun div(factor: Number): Vec3D = Vec3D(x / factor.toFloat(), y / factor.toFloat(), z / factor.toFloat())
    operator fun divAssign(vec: Vec3D) {
        this.x /= vec.x
        this.y /= vec.y
        this.z /= vec.z
    }

    fun plusElementwise(vec: Vec3D): Vec3D = Vec3D(this.x + vec.x, this.y + vec.y, this.z + vec.z)
    fun minusElementwise(vec: Vec3D): Vec3D = Vec3D(this.x - vec.x, this.y - vec.y, this.z - vec.z)
    fun timesElementwise(vec: Vec3D): Vec3D = Vec3D(this.x * vec.x, this.y * vec.y, this.z * vec.z)
    fun divElementwise(vec: Vec3D): Vec3D = Vec3D(this.x / vec.x, this.y / vec.y, this.z / vec.z)
    fun rotateX(angle: Float) {
        val tmpy = y * cos(angle) + z * sin(angle)
        z = -y * sin(angle) + z * cos(angle)
        y = tmpy
    }

    fun rotateY(angle: Float) {
        val tmpz = z * cos(angle) + x * sin(angle)
        x = -z * sin(angle) + x * cos(angle)
        z = tmpz
    }

    fun rotateZ(angle: Float) {
        val tmpx = x * cos(angle) + y * sin(angle)
        y = -x * sin(angle) + y * cos(angle)
        x = tmpx
    }

    fun to2D(): Vec2D = Vec2D(x, y)
}

operator fun Number.times(vec: Vec3D): Vec3D = vec.times(this)