package com.example.pocketrocket.gui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.example.pocketrocket.control.GameMaster
import com.example.pocketrocket.model.StarField

class StarFieldSurfaceView(c: Context, attr: AttributeSet) : GameSurfaceView(c, attr) {
    init {
        gameMaster = GameMaster(holder)
        gameMaster!!.setupWorld(StarField(width, height, 50))
        Log.d("SFSV", "$width $height")
    }
}