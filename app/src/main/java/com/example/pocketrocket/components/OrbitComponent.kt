package com.example.pocketrocket.components

data class OrbitComponent(
    var r0: Float = 0f,
    var t0: Float = 0f,
    var vr: Float = 0f,
    var va: Float = 0f,
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}