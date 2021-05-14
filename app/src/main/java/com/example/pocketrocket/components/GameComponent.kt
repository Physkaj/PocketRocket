package com.example.pocketrocket.components

interface IGameComponent {
    val componentID: CidType
}

abstract class GameComponentCompanion : IGameComponent {
    companion object {
        var nextID: CidType = 0
    }

    override val componentID: CidType by lazy { nextID++ }
}