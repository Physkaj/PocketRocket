package com.example.pocketrocket.utils

import android.graphics.Rect

abstract class ScreenProperties {
    abstract val width: Int
    abstract val height: Int
    fun getRect(): Rect = Rect(0, 0, width, height)
    fun screenCoordinates(x: Float, y: Float) =
        Vec2D(width * 0.5f + height * 0.5f * x, height * 0.5f * (1f - y))

    fun screenCoordinates(vec: Vec2D) = screenCoordinates(vec.x, vec.y)

    fun screenRadius(r: Float) = height * 0.5f * r
}

class MutableScreenProperties(private var _width: Int = 0, private var _height: Int = 0) : ScreenProperties() {
    constructor(x: Number, y: Number) : this(x.toInt(), y.toInt())
    constructor(screenSize: Rect) : this(screenSize.width(), screenSize.height())

    override val width: Int
        get() = _width
    override val height: Int
        get() = _height

    fun setWidth(value: Number) {
        _width = value.toInt()
    }

    fun setHeight(value: Number) {
        _height = value.toInt()
    }
}