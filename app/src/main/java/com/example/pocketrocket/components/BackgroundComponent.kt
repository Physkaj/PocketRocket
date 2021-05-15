package com.example.pocketrocket.components

import android.graphics.PorterDuff

data class BackgroundComponent(
    val drawMode: PorterDuff.Mode = PorterDuff.Mode.SRC
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}