package com.example.pocketrocket.components

import kotlin.math.sqrt

data class OrbitComponent(
    // Semi-major axis, distance from center of ellipse to apoapsis
    var a: Float = 0f,
    // Semi-minor axis, distance from center of ellipse to periapsis
    var b: Float = 0f,
    // Angle between orbital plane and reference plane
    var inclination: Float = 0f,
    // Argument of apoapsis, angle between reference direction and apoapsis ignoring inclination
    var argApoapsis: Float = 0f,
    // Current position in orbit
    var arg: Float = 0f,
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()

    // Linear eccentricity
    val c: Float
        get() = sqrt(a * a - b * b)

    // Eccentricity, how much it differs from a circle
    val e: Float
        get() = c / a
}