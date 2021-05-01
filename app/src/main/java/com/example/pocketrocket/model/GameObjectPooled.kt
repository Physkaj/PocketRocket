package com.example.pocketrocket.model

interface GameObjectPooled {
    var pool: GameObjectPool<GameObjectPooled>?
}
