package com.example.pocketrocket.components

data class BackgroundComponent(val isBackground: Boolean = true) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}