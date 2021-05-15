package com.example.pocketrocket.components

import android.graphics.Canvas

data class ShapeComponent(
    var shapeType: ShapeType = ShapeType.CIRCLE,
    var parameter1: Float = 0f,
    var parameter2: Float = 0f
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
    enum class ShapeType {
        CIRCLE,
        LINE,
        RECTANGLE
    }

    var r: Float
        get() = parameter1
        set(value) {
            parameter1 = value
        }
    var x: Float
        get() = parameter1
        set(value) {
            parameter1 = value
        }
    var y: Float
        get() = parameter2
        set(value) {
            parameter2 = value
        }
}