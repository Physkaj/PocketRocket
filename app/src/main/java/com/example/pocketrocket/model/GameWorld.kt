package com.example.pocketrocket.model

abstract class GameWorld(var width: Int, var height: Int, val tickMillis: Long) : GameObject() {
    abstract fun reconfigure(width: Int, height: Int)
}

