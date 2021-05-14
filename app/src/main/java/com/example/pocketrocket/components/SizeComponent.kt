package com.example.pocketrocket.components

data class SizeComponent(
    var width: Float = 0f,
    var height: Float = 0f
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}