package com.example.pocketrocket.model

import android.graphics.Canvas

abstract class GameObject {
    abstract fun draw(c: Canvas)
    abstract fun update()
}