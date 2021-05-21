package com.example.pocketrocket.components

data class DebugComponent(
    val debug: Boolean = true
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}