package com.example.pocketrocket.components

import android.graphics.Typeface
import androidx.annotation.ColorInt
import com.example.pocketrocket.utils.Vec2D

data class TextComponent(
    var text: String = "",
    var textPosition: Vec2D = Vec2D(0f, 0f),
    var useRelativeCoordinates: Boolean = false, // If a parent is present
    @ColorInt var textColor: Int = 0,
    var textSize: Float = 14f,
    var typeface: Typeface = Typeface.DEFAULT
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}