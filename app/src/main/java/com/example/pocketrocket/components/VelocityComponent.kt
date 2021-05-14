package com.example.pocketrocket.components

data class VelocityComponent(var vx: Float = 0f, var vy: Float = 0f) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}