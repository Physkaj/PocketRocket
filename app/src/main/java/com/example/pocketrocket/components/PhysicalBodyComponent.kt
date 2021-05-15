package com.example.pocketrocket.components

import com.example.pocketrocket.utils.Vec2D

data class PhysicalBodyComponent(
    var pos: Vec2D = Vec2D(0f, 0f),
    var vel: Vec2D = Vec2D(0f, 0f),
    var acc: Vec2D = Vec2D(0f, 0f)
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}