package com.example.pocketrocket.utils

abstract class ScreenProperties {
    abstract val width: Int
    abstract val height: Int
    fun screenCoordinates(x: Float, y: Float) =
        Vec2D(width * 0.5f + height * 0.5f * x, height * 0.5f * (1f - y))

    fun screenCoordinates(vec: Vec2D) = screenCoordinates(vec.x, vec.y)

    fun screenRadius(r: Float) = height * 0.5f * r
}

class MutableScreenProperties(private var _width: Int = 0, private var _height: Int = 0) : ScreenProperties() {
    override val width: Int
        get() = _width
    override val height: Int
        get() = _height

    fun setWidth(value: Int) {
        _width = value
    }

    fun setHeight(value: Int) {
        _height = value
    }
}