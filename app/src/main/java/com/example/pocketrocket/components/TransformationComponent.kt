package com.example.pocketrocket.components

data class TransformationComponent(
    var translationX: Float = 0f,
    var translationY: Float = 0f,
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var rotate: Float = 0f,
    var mirror: Boolean = false
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}