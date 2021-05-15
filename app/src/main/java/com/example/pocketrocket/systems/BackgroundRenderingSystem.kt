package com.example.pocketrocket.systems

import android.graphics.Canvas
import com.example.pocketrocket.components.BackgroundComponent
import com.example.pocketrocket.components.ColorComponent
import com.example.pocketrocket.managers.ECSCallback
import java.util.*

class BackgroundRenderingSystem(callback: ECSCallback) : GameSystem(callback) {
    override fun appliesToSignature(signature: BitSet): Boolean {
        return signature.get(BackgroundComponent.componentID) && signature.get(ColorComponent.componentID)
    }

    fun drawBackground(canvas: Canvas) {
        for (eid in entityList) {
            val background = callback.getComponent<BackgroundComponent>(eid, BackgroundComponent.componentID)
            val color = callback.getComponent<ColorComponent>(eid, ColorComponent.componentID)
            canvas.drawColor(color.color, background.drawMode)
        }
    }
}