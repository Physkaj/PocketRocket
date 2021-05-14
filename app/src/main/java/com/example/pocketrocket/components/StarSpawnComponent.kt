package com.example.pocketrocket.components

data class StarSpawnComponent(
    var spawnProbabilityPerSec: Float = 0f,
    var nextSpawn: Float = 0f
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}