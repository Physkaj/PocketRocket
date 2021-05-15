package com.example.pocketrocket.components

import android.graphics.Paint

data class TextComponent(
    var text: String = "",
    var textPosition: Pair<Float, Float> = Pair(0f, 0f),
    var useRelativeCoordinates: Boolean = true,
    var textPaint: Paint? = null // Lazy, should eventually be improved
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}