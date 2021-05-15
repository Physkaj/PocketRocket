package com.example.pocketrocket.components

data class StarSpawnComponent(
    var nextSpawnMinSec: Float = 0f,
    var nextSpawnMaxSec: Float = 1f,
    var nextSpawn: Float = 0f,
    var starRadialVelocity: Float = 0.01f,
    var starAngularVelocity: Float = 0.5f,
    var starRadius: Float = 0.01f
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}