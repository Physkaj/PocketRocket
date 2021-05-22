package com.example.pocketrocket.components

import android.graphics.Bitmap

data class BitmapComponent(
    var bitmap: Bitmap? = null
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}