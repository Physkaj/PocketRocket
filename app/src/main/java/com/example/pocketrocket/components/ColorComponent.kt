package com.example.pocketrocket.components

import androidx.annotation.ColorInt

data class ColorComponent(
    // Int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    @ColorInt var color: Int = 0
) : IGameComponent by Companion {
    companion object : GameComponentCompanion()
}