package com.example.pocketrocket.components

import android.graphics.Rect
import com.example.pocketrocket.utils.Vec2D

enum class GradientType {
    LINEAR,
    RADIAL,
    SWEEP
}

data class GradientComponent(
    var bitmapWidth: Int = 1,
    var bitmapHeight: Int = 1,
    var gradientFrom: Vec2D = Vec2D(),
    var gradientTo: Vec2D = Vec2D(),
    var colors: Collection<Int> = listOf(),
    var gradientType: GradientType = GradientType.LINEAR
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()

    var gradientR: Vec2D
        get() = gradientTo
        set(value) {
            gradientTo = value
        }
}