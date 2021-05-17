package com.example.pocketrocket.components

import com.example.pocketrocket.utils.Vec3D

data class PhysicalBodyComponent(
    //var pos: Vec2D = Vec2D(0f, 0f),
    val vel: Vec3D = Vec3D(0f, 0f, 0f),
    val acc: Vec3D = Vec3D(0f, 0f, 0f)
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}