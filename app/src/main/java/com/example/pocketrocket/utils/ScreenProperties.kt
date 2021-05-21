package com.example.pocketrocket.utils

abstract class ScreenProperties {
    abstract val width: Float
    abstract val height: Float
    fun screenCoordinates(x: Float, y: Float) =
        Vec2D(width * 0.5f + height * 0.5f * x, height * 0.5f * (1f - y))

    fun screenCoordinates(vec: Vec2D) = screenCoordinates(vec.x, vec.y)

    fun screenRadius(r: Float) = height * 0.5f * r
}

class MutableScreenProperties(private var _width: Float = 0f, private var _height: Float = 0f) : ScreenProperties() {
    constructor(x: Number, y: Number) : this(x.toFloat(), y.toFloat())
    constructor(screenSize: Vec2D) : this(screenSize.x, screenSize.y)

    override val width: Float
        get() = _width
    override val height: Float
        get() = _height

    fun setWidth(value: Number) {
        _width = value.toFloat()
    }

    fun setHeight(value: Number) {
        _height = value.toFloat()
    }
}