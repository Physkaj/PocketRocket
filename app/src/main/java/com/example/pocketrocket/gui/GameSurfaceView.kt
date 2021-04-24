package com.example.pocketrocket.gui

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pocketrocket.control.GameMaster

open class GameSurfaceView(c: Context, attr: AttributeSet) : SurfaceView(c, attr),
    SurfaceHolder.Callback {
    var gameMaster: GameMaster? = null

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        gameMaster?.startWorld()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        gameMaster?.reconfigureWorld(width, height)
        gameMaster?.startWorld()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameMaster?.stopWorld()
    }
}