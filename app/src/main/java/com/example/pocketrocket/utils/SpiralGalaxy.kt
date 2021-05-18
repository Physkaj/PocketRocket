package com.example.pocketrocket.utils

import com.example.pocketrocket.components.OrbitComponent
import com.example.pocketrocket.components.PositionComponent
import kotlin.math.*
import kotlin.random.Random

object SpiralGalaxy {
    var nArms: Int = 3
    var minApoapsis: Float = 0.1f
    var maxApoapsis: Float = 1f
    var minEcc: Float = 0.3f
    var maxEcc: Float = 0.35f
    var minArg: Float = 0.0f
    var maxAngle: Float = 2f * PI.toFloat()
    val k1: Float
        get() = minApoapsis
    val k2: Float
        get() = ln(maxApoapsis / minApoapsis) / maxAngle

    private fun Random.nextFloat(minValue: Number, maxValue: Number): Float =
        Random.nextDouble(minValue.toDouble(), maxValue.toDouble()).toFloat()

    fun setupStar(posComp: PositionComponent, orbComp: OrbitComponent) {
        // Apoapsis r = a+c = a + ecc*a = a*(1+ecc)
        val apoapsis: Float = Random.nextFloat(minApoapsis, maxApoapsis)
        // Logarithmic spiral equation
        var argApoapsis: Float = ln(apoapsis / k1) / k2 + minArg
        // Eccentricity
        val ecc = Random.nextFloat(minEcc, maxEcc)
        // Semi-major axis
        val a = apoapsis / (1 + ecc)
        // Semi-minor axis
        val b = sqrt(a * a * (1 - ecc * ecc))
        // Linear eccentricity
        val c = a * ecc

        // Periapsis in y-direction and apoapsis in x-direction
        val arg = Random.nextFloat(0f, twoPI)
        val randX = a * cos(arg) + c
        val randY = b * sin(arg)
        // Create to different arms
        argApoapsis += twoPI / nArms * Random.nextInt(0, nArms)
        // Rotate according to the spiral position
        posComp.pos.x = (cos(argApoapsis) * randX + sin(argApoapsis) * randY)
        posComp.pos.y = (-sin(argApoapsis) * randX + cos(argApoapsis) * randY)
        // Orbital parameters
        orbComp.a = a
        orbComp.b = b
        orbComp.inclination = 0f
        orbComp.argApoapsis = argApoapsis
        orbComp.arg = arg
    }
}