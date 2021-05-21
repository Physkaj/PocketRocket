package com.example.pocketrocket.components

data class DebugComponent(
    var debug: Boolean = true
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}