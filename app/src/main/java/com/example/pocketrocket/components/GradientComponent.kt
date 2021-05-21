package com.example.pocketrocket.components

import android.graphics.Shader

enum class GradientType {
    LINEAR,
    RADIAL,
    SWEEP
}

data class GradientComponent(
    var colors: MutableList<Int> = mutableListOf(),
    var gradientType: GradientType = GradientType.LINEAR,
    var shader: Shader? = null
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}