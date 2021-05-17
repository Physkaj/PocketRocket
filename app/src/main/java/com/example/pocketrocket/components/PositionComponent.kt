package com.example.pocketrocket.components

import com.example.pocketrocket.utils.Vec3D

data class PositionComponent(
    val pos: Vec3D = Vec3D(0f, 0f, 0f)
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}