package com.example.pocketrocket.components

data class GravityComponent(
    var mass: Float = 0f
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}