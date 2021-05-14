package com.example.pocketrocket.components

data class DirectionComponent(
    var x: Float = 0f,
    var y: Float = 0f
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}