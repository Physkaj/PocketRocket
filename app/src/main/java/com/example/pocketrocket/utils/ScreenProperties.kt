package com.example.pocketrocket.utils

import android.graphics.Rect
import android.graphics.RectF
import androidx.core.graphics.toRectF

abstract class ScreenProperties {
    abstract val width: Int
    abstract val height: Int
    fun getRect(): Rect = Rect(0, 0, width, height)
    fun getRectF(): RectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
    fun screenCoordinates(x: Float, y: Float, rect: Rect): Vec2D = screenCoordinates(x, y, rect.toRectF())
    fun screenCoordinates(x: Float, y: Float, rect: RectF = getRectF()): Vec2D =
        Vec2D(rect.left + 0.5f * (rect.width() + rect.height() * x), rect.top + rect.height() * 0.5f * (1f - y))

    fun screenCoordinates(vec: Vec2D, rect: Rect) = screenCoordinates(vec.x, vec.y, rect)
    fun screenCoordinates(vec: Vec2D, rect: RectF = getRectF()) = screenCoordinates(vec.x, vec.y, rect)

    fun screenRadius(r: Float, rect: Rect = getRect()) = rect.height() * 0.5f * r
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